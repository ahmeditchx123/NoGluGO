package com.noglugo.mvp.service.impl;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.noglugo.mvp.service.FileUploadService;
import com.noglugo.mvp.service.dto.FileUploadPathDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private String baseUrl = "http://localhost:8080/api/v1/upload/get/images";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadService.class);

    @Override
    public FileUploadPathDTO uploadFile(MultipartFile file) {
        LOGGER.debug("uploading file {}", file.getOriginalFilename());
        FileUploadPathDTO fileUploadPathDTO = new FileUploadPathDTO();
        String uploadDirectory = "src/main/webapp/uploads";
        Path uploadPath = Paths.get(uploadDirectory);
        try {
            Files.createDirectories(uploadPath);
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            String imageUrl = baseUrl + "/" + file.getOriginalFilename();
            fileUploadPathDTO.setFileName(file.getOriginalFilename());
            fileUploadPathDTO.setFilePath(imageUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileUploadPathDTO;
    }
}
