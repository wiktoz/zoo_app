package com.example.blog_app_new.networksModels;

import java.util.List;

public class PostRequest {
    private String title;
    private String content;
    private List<String> photos;

    public PostRequest(String title, String content, List<String> photos) {
        this.title = title;
        this.content = content;
        this.photos = photos;

    }
}
