package com.example.blog_app_new.models;

public class LoginResponse {
    private boolean login;
    private String accessToken;
    private String refreshToken;

    public boolean isLogin() {
        return login;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
