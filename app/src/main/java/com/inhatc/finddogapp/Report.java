package com.inhatc.finddogapp;

public class Report {
    private double latitude;
    private double longitude;
    private String description;
    private String imageUrl;

    // Firebase에서 객체로 매핑할 때 반드시 필요한 기본 생성자
    public Report() {
    }

    public Report(double latitude, double longitude, String description, String imageUrl) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getter & Setter

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}