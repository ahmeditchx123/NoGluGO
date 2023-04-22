package com.noglugo.mvp.service;

import com.noglugo.mvp.service.dto.FileUploadPathDTO;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    FileUploadPathDTO uploadFile(MultipartFile file);
}
