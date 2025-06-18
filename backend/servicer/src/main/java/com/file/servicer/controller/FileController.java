package com.file.servicer.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.servicer.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@CrossOrigin
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * Endpoint to upload a file.
     *
     * @param file the file to be uploaded
     * @return a map containing the download URL of the uploaded file
     * @throws IOException if an I/O error occurs
     */
    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
        try {
            String fileId = fileService.storeFile(file);
            String downloadUrl = "/download/" + fileId;
            return Map.of("url", downloadUrl);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with error:", e);
        }
    }

    /**
     * Endpoint to download a file by its ID.
     *
     * @param fileId the unique file ID
     * @return ResponseEntity containing the file resource
     * @throws IOException if an I/O error occurs
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        try {
            return fileService.downloadedFile(fileId);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with error:", e);
        }
    }
}
