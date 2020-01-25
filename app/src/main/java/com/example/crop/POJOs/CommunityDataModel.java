package com.example.crop.POJOs;

public class CommunityDataModel {

    int id;
    String name;
    int image;

    public CommunityDataModel(int image){
        this.image=image;
    }
    public CommunityDataModel(int id, int image, String name){
        this.id=id;
        this.image=image;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
