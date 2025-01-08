package com.example.blog_app_new.CModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {

    @SerializedName("post_id")
    @Expose
    public String post_id;

    @SerializedName("group_id")
    @Expose
    public String group_id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("content")
    @Expose
    public String content;

    // Jeśli backend zwraca user jako np. "Jan Kowalski"
    @SerializedName("user")
    @Expose
    public String user;

    @SerializedName("created_at")
    @Expose
    public String created_at;

    @SerializedName("updated_at")
    @Expose
    public String updated_at;

    // Zależnie od potrzeb możesz przechowywać ID lub listę ID
    @SerializedName("photos")
    @Expose
    public List<String> photos;

    @SerializedName("comments")
    @Expose
    public List<String> comments;

    @SerializedName("ratings")
    @Expose
    public List<String> ratings;

    @SerializedName("notifications")
    @Expose
    public List<String> notifications;

    @Override
    public String toString() {
        return "Post{" +
                "post_id='" + post_id + '\'' +
                ", group_id='" + group_id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user='" + user + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", photos=" + photos +
                ", comments=" + comments +
                ", ratings=" + ratings +
                ", notifications=" + notifications +
                '}';
    }
}
