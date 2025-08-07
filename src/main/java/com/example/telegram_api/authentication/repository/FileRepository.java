package com.example.telegram_api.authentication.repository;

import com.example.telegram_api.model.entity.FileStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileStore,Long> {
    FileStore findByFileName(String fileName);
}
