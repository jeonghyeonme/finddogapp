package com.inhatc.finddogapp;

public class Report {
    public double latitude;
    public double longitude;
    public String description;
    public String imageUrl;

    public Report() {}  // Firebase requires no-arg constructor

    public Report(double latitude, double longitude, String description, String imageUrl) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
    }
}