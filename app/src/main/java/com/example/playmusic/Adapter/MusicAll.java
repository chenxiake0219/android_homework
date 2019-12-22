package com.example.playmusic.Adapter;

public class MusicAll {
    private String name;
    private int file;
    private int imageId;

    public MusicAll(String name, int file, int imageId){
        this.name = name;
        this.file = file;
        this.imageId = imageId;
    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getFile(){
        return file;
    }

    public void setFile(int file){
        this.file = file;
    }

    public int getImageId(){
        return imageId;
    }

    public void setImageId(int imageId){
        this.imageId = imageId;
    }
}
