package com.noglugo.mvp.service.dto;

public class JwtDTO {

    private String token;

    public JwtDTO(String token) {
        this.token = token;
    }

    public JwtDTO() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
