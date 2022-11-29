package com.example.listenup;

public class Data {

    String name;
    String image;
    String song;
    String id;

    public Data() {
    }

    public Data(String name, String image, String song, String id) {
        this.name = name;
        this.image = image;
        this.song = song;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
