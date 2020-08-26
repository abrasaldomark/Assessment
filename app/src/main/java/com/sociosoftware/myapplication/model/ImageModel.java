package com.sociosoftware.myapplication.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ImageModel extends RealmObject {

    @PrimaryKey
    private long id;
    private String imageUrl;
    private String currentDate;
    private String currentTime;

    public ImageModel() {
    }

    public ImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageModel(String imageUrl, String currentDate, String currentTime) {
        this.imageUrl = imageUrl;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
