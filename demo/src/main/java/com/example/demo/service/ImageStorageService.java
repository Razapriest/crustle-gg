package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final String uploadDir = "uploads/images/";

    public String saveImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return null;
        }

        Files.createDirectories(Paths.get(uploadDir));

        String extension = getExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + "." + extension;

        Path path = Paths.get(uploadDir + filename);

        file.transferTo(path.toFile());

        return "/" + uploadDir + filename;
    }

    private String getExtension(String filename) {
        if (filename == null) return "png";
        int dot = filename.lastIndexOf(".");
        return (dot == -1) ? "png" : filename.substring(dot + 1);
    }
}