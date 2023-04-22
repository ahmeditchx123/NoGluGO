package com.noglugo.mvp.service.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ImageDTO implements Serializable {

    @NotBlank
    @NotNull
    private String imagPath;

    public ImageDTO() {}

    public ImageDTO(String imagPath) {
        this.imagPath = imagPath;
    }

    public String getImagPath() {
        return imagPath;
    }

    public void setImagPath(String imagPath) {
        this.imagPath = imagPath;
    }
}
