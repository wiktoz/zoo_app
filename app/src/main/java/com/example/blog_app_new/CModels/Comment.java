package com.example.blog_app_new.CModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("comment_id")
    @Expose
    public String comment_id;
    @SerializedName("post_id")
    @Expose
    public String post_id;
    @SerializedName("user_id")
    @Expose
    public String user_id;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("username")
    @Expose(deserialize = true, serialize = false)
    public String username;

    @Override
    public String toString() {
        return "Comment{" +
                "comment_id='" + comment_id + '\'' +
                ", post_id='" + post_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", content='" + content + '\'' +
                ", created_at='" + created_at + '\'' +
                ", name='" + username + '\'' +
                '}';
    }
}