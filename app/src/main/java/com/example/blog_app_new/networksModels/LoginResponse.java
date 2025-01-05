package com.example.blog_app_new.networksModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("login")
    @Expose
    public boolean login;

    @SerializedName("message")
    @Expose
    public String message;
}
