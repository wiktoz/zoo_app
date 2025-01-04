package com.example.blog_app_new;

public class Group {
    private String name;
    private String description;

    // Konstruktor
    public Group(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Gettery
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
