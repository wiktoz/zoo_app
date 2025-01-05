package com.example.blog_app_new.CModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("notification_id")
    @Expose
    public String notification_id;

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("post_id")
    @Expose
    public String post_id;

    @SerializedName("content")
    @Expose
    public String content;

    @SerializedName("viewed")
    @Expose
    public String viewed;

    @SerializedName("created_at")
    @Expose
    public String created_at;


    @Override
    public String toString() {
        return "Notification{" +
                "content='" + content + '\'' +
                '}';
    }
}