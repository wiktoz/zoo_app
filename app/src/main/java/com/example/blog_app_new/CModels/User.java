package com.example.blog_app_new.networksModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("surname")
    private String surname;

    @SerializedName("groups")
    private List<String> groups;

    @SerializedName("posts")
    private List<Integer> posts;

    @SerializedName("comments")
    private List<Integer> comments;

    @SerializedName("ratings")
    private List<Integer> ratings;

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public List<String> getGroups() {
        return groups;
    }

    public List<Integer> getPosts() {
        return posts;
    }

    public List<Integer> getComments() {
        return comments;
    }

    public List<Integer> getRatings() {
        return ratings;
    }
}
