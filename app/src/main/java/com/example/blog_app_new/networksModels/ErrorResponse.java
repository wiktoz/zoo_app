package com.example.blog_app_new.networksModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorResponse {

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("msg")
    @Expose
    public String msg;
}
