package com.noglugo.mvp.web.rest;

import com.noglugo.mvp.service.FileUploadService;
import com.noglugo.mvp.service.dto.FileUploadPathDTO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/upload")
public class UploadNoglugoResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadNoglugoResource.class);

    private final FileUploadService fileUploadService;

    public UploadNoglugoResource(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @RequestMapping(path = "/images", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadPathDTO> uploadFile(@RequestParam("image") MultipartFile image) {
        LOGGER.debug("Rest Request to upload file POST/ api/v1/upload ...");
        return ResponseEntity.status(HttpStatus.OK).body(fileUploadService.uploadFile(image));
    }

    @GetMapping("/get/images/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            String uploadDirectory = "src/main/webapp/uploads";
            Path uploadPath = Paths.get(uploadDirectory);
            Path filePath = uploadPath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
                return ResponseEntity
                    .ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
