package com.example.blog_app_new.CModels;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {
    @SerializedName("group_id")
    @Expose
    public String group_id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("posts")
    @Expose
    public List<String> posts;
}