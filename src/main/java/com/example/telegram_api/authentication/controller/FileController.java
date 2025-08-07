package com.example.telegram_api.authentication.controller;

import com.example.telegram_api.authentication.service.FilesService;
import com.example.telegram_api.exception.NotFoundExceptionClass;
import com.example.telegram_api.model.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin
@SecurityRequirement(name = "bearerAuth")
public class FileController {
    @Value("${file_path}")
    private String file_path;

    private final FilesService filesService;

    @GetMapping("/get-file")
    public ApiResponse<?> getFile(@RequestParam String fileName) {
        return ApiResponse.builder()
                .payload(filesService.getFileByFileName(fileName))
                .date(LocalDateTime.now())
                .status(200)
                .message("successfully fetched")
                .build();
    }

    @GetMapping("/images")
    public ResponseEntity<?> getImagesByFileName(@RequestParam String fileName) throws NotFoundExceptionClass {
        try{
            Resource file = filesService.getImagesByFileName(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
        }catch (Exception e){
            throw new NotFoundExceptionClass("this file isn't exist.");
        }
    }


    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> fileUpload(@RequestParam MultipartFile imageFile) throws IOException {
        return ApiResponse.builder()
                .payload(filesService.fileUpload(imageFile))
                .date(LocalDateTime.now())
                .status(200)
                .message("successfully uploaded")
                .build();
    }

    @GetMapping("/view-files")
    public ResponseEntity<InputStreamResource> fileViewByFileName(@RequestParam String fileName) throws IOException {
        try {
            Path path = Paths.get(file_path + "/" + fileName);
            // Read the Excel file as InputStream
            InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            // Set the content type to application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (FileNotFoundException e) {
            throw new NotFoundExceptionClass("This file does not exist.");
        }
    }
}
