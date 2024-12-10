package com.isa.customer_service.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {

    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "uploads"); // Use absolute path

    public String saveImage(MultipartFile image) throws IOException {
        // Create the upload directory if it doesn't exist
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // Generate a unique file name based on current timestamp
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path targetPath = uploadDir.resolve(fileName);

        // Save the file
        Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Return the public URL of the image
        return "/uploads/" + fileName;  // This is the URL your React frontend will use
    }

}

