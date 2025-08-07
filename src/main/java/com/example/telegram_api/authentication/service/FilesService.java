package com.example.telegram_api.authentication.service;

import com.example.telegram_api.authentication.repository.FileRepository;
import com.example.telegram_api.exception.NotFoundExceptionClass;
import com.example.telegram_api.model.entity.FileStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FilesService {

    @Value("${file_path}")
    private String file_path;

    @Value("${baseUrl_img}")
    private String base_url;

    private final FileRepository fileRepository;

    public FilesService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Resource getImagesByFileName(String fileName) throws NotFoundExceptionClass, IOException {
        Path path = Paths.get(file_path + "/" + fileName);

        //get and response as image
        return new ByteArrayResource(Files.readAllBytes(path));
    }

    public Object fileUpload(MultipartFile file) throws IOException {
        Path root = Paths.get(file_path);

        // Validate file
        validateFile(file);

        FileStore documentFile = new FileStore();
        documentFile.setUploadDate(LocalDateTime.now());

        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);

            // Create encrypted/encoded filename
            String encryptedFileName = generateEncryptedFileName(originalFileName, fileExtension);

            // Create directory if not exists
            createDirectoryIfNotExists(root);

            // Get unique filename if duplicate exists
            String uniqueFileName = getUniqueFileName(root, encryptedFileName, fileExtension);

            // Validate path traversal security
            Path destinationFile = validateDestinationPath(root, uniqueFileName);

            // Save file
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // Set file properties
            documentFile.setOriginalFileName(originalFileName);
            documentFile.setFileName("WEBVIEW_" +uniqueFileName);
            documentFile.setUrl(base_url + uniqueFileName);
            documentFile.setFileSize(String.valueOf(file.getSize()));

            return fileRepository.save(documentFile);

        } catch (IOException e) {
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        // Check file size (e.g., max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size cannot exceed 10MB");
        }

        // Validate file extension
        if (!isAllowedFileType(fileName)) {
            throw new IllegalArgumentException("File type not allowed");
        }
    }

    private boolean isAllowedFileType(String fileName) {
        String[] allowedExtensions = {
                ".jpg", ".jpeg", ".png", ".gif", ".webp",  // Images
                ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",  // Documents
                ".mp3", ".mp4", ".webm",  // Media
                ".csv", ".zip", ".rar", ".tar"  // Others
        };

        String lowerFileName = fileName.toLowerCase();
        return Arrays.stream(allowedExtensions)
                .anyMatch(lowerFileName::endsWith);
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    private String generateEncryptedFileName(String originalFileName, String extension) {
        try {
            // Using UUID + timestamp
            String timestamp = String.valueOf(System.currentTimeMillis());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            return uuid + "_" + timestamp + extension;

        } catch (Exception e) {
            return "file_" + System.currentTimeMillis() + extension;
        }
    }

    private void createDirectoryIfNotExists(Path root) throws IOException {
        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }
    }

    private String getUniqueFileName(Path root, String fileName, String extension) {
        Path destinationFile = root.resolve(fileName);

        if (!Files.exists(destinationFile)) {
            return fileName;
        }

        // If file exists, append counter
        String baseName = fileName.replace(extension, "");
        int counter = 1;

        do {
            fileName = baseName + "_" + counter + extension;
            destinationFile = root.resolve(fileName);
            counter++;
        } while (Files.exists(destinationFile));

        return fileName;
    }

    private Path validateDestinationPath(Path root, String fileName) throws IOException {
        Path destinationFile = root.resolve(fileName).normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(root.toAbsolutePath())) {
            throw new IOException("Cannot store file outside current directory - potential path traversal attack");
        }

        return destinationFile;
    }

    public FileStore getFileByFileName(String fileName) {
        if(fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        FileStore fileStore = fileRepository.findByFileName(fileName);

//        System.out.println("fileStore >>>>" + fileStore);

        if(fileStore == null) {
            throw new NotFoundExceptionClass("File not found");
        }

        return fileStore;
    }
}
