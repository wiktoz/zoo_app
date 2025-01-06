package com.example.blog_app_new.CModels;

import com.google.gson.annotations.SerializedName;

public class AverageRatingResponse {
    @SerializedName("average")
    private double average;

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
