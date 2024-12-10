package com.isa.customer_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.MimeTypeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Value("${image.upload-dir}")
    private String uploadDir;

    @GetMapping("/profile/{imageName}")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable String imageName) throws IOException {
        Path imagePath = Paths.get(uploadDir).resolve(imageName);
        File imageFile = imagePath.toFile();

        if (!imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileInputStream fileInputStream = new FileInputStream(imageFile);
        InputStreamResource resource = new InputStreamResource(fileInputStream);

        return ResponseEntity.ok()
                .contentType(Files.probeContentType(imagePath).startsWith("image") ?
                        MediaType.IMAGE_JPEG : MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageFile.getName() + "\"")
                .body(resource);
    }
}
