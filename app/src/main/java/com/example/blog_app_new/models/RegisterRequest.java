package com.example.blog_app_new.models;

public class RegisterRequest {
    private String name;
    private String surname;
    private String email;
    private String password;

    public RegisterRequest(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
