package com.example.blog_app_new.networksModels;

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
