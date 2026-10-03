package com.example.news_backend_api.service.impl;

import com.example.news_backend_api.common.AppConstants;
import com.example.news_backend_api.dto.response.FileUploadResponse;
import com.example.news_backend_api.exception.BadRequestException;
import com.example.news_backend_api.exception.FileStorageException;
import com.example.news_backend_api.exception.ResourceNotFoundException;
import com.example.news_backend_api.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageServiceImpl(@Value("${app.file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public FileUploadResponse storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Failed to upload: file is empty.");
        }

        String originalFileName = StringUtils.cleanPath(
            file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"
        );

        // Normalize filename to prevent path traversal attacks
        if (originalFileName.contains("..")) {
            throw new BadRequestException("Filename contains invalid path sequence: " + originalFileName);
        }

        // Validate extension
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf('.');
        if (dotIndex > 0) {
            fileExtension = originalFileName.substring(dotIndex).toLowerCase();
        }

        final String ext = fileExtension;
        boolean allowed = Arrays.stream(AppConstants.ALLOWED_FILE_EXTENSIONS)
            .anyMatch(allowedExt -> allowedExt.equalsIgnoreCase(ext));

        if (!allowed) {
            throw new BadRequestException("File type not allowed. Allowed types: " +
                String.join(", ", AppConstants.ALLOWED_FILE_EXTENSIONS));
        }

        // Generate unique filename
        String safeBaseName = originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        String uniqueFileName = UUID.randomUUID().toString() + "_" + safeBaseName;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(uniqueFileName)
                .toUriString();

            return new FileUploadResponse(
                uniqueFileName,
                fileDownloadUri,
                file.getContentType(),
                file.getSize()
            );
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            // Verify file is within the allowed upload directory
            if (!filePath.startsWith(this.fileStorageLocation)) {
                throw new BadRequestException("Invalid file path: " + fileName);
            }

            if (!Files.exists(filePath)) {
                throw new ResourceNotFoundException("File", "name", fileName);
            }

            Files.delete(filePath);
        } catch (IOException ex) {
            throw new FileStorageException("Could not delete file " + fileName, ex);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            if (!filePath.startsWith(this.fileStorageLocation)) {
                throw new BadRequestException("Invalid file path: " + fileName);
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File", "name", fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResourceNotFoundException("File", "name", fileName);
        }
    }
}
