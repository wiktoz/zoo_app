package com.example.blog_app_new.models;

public class TokenResponse {
    private boolean refresh;
    private String accessToken;

    public boolean isRefresh() {
        return refresh;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
