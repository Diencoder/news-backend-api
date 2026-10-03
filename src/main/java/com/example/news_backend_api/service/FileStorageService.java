package com.example.news_backend_api.service;

import com.example.news_backend_api.dto.response.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    FileUploadResponse storeFile(MultipartFile file);

    void deleteFile(String fileName);

    Resource loadFileAsResource(String fileName);
}
