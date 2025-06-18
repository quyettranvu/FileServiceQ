package com.file.servicer.service;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.servicer.POJO.FileMetaData;

@Service
public class FileService {
    private final Map<String, FileMetaData> fileStore = new ConcurrentHashMap<>();
    private final Path uploadDir = Paths.get("uploads");

    /**
     * Create directory for file uploads if it does not exist.
     */
    public FileService() throws IOException {
        Files.createDirectories(uploadDir);
    }

    /**
     * Store the uploaded file and return a unique file ID.
     *
     * @param file the file to be stored
     * @return the unique file ID
     * @throws IOException if an I/O error occurs
     */
    public String storeFile(MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        Path filePath = uploadDir.resolve(fileName);

        // Save the file to the filesystem
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Create metadata and store it, default now also downloaded
        FileMetaData metaData = new FileMetaData(fileId, fileName, filePath.toString(), LocalDateTime.now());
        fileStore.put(fileId, metaData);
        
        return fileId;
    }

    /**
     * Retrieve the file metadata by its ID.
     *
     * @param fileId the unique file ID
     * @return the file metadata, or null if not found
     */
    public ResponseEntity<Resource> downloadedFile(String fileId) throws IOException {
        FileMetaData metaData = fileStore.get(fileId);
        // Not found metadata in store
        if (metaData == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the file exists in the filesystem
        Path filePath = Paths.get(metaData.getPath());
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        // Update last downloaded time
        metaData.setLastDownloaded(LocalDateTime.now());
        // Return the file as a resource
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + metaData.getOriginalFileName() + "\"")
                .body(resource);
    }

    /**
     * Delete files that have not been downloaded within the specified duration.
     *
     * @param maxAge the maximum age of files to keep
     */
    public void deleteOldFiles(Duration maxAge) {
        LocalDateTime cutoffTime = LocalDateTime.now().minus(maxAge);
        fileStore.entrySet().removeIf(entry -> {
            FileMetaData metaData = entry.getValue();
            if (metaData.getLastDownloaded().isBefore((cutoffTime))) {
                try {
                    Files.deleteIfExists(Paths.get(metaData.getPath()));
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
    }
}
