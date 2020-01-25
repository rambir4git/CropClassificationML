package com.example.crop.POJOs;

import android.graphics.Bitmap;

public class SavedImages {
    private String location;
    private String cropType;
    private String date;
    private Bitmap image;

    public SavedImages(String location, String cropType, String date, Bitmap image) {
        this.location = location;
        this.cropType = cropType;
        this.date = date;
        this.image = image;
    }

    public String getLocation() {
        return location;
    }

    public String getCropType() {
        return cropType;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getImage() {
        return image;
    }
}