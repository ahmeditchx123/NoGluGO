package com.noglugo.mvp.service.dto;

import java.io.Serializable;

public class FileUploadPathDTO implements Serializable {

    private String fileName;

    private String filePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
