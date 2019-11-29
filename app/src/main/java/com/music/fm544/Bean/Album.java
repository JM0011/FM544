package com.music.fm544.Bean;


import java.io.Serializable;

public class Album implements Serializable{
    private String albumName;
    private String singer;
    private String picPath;

    public Album() {
    }



    public Album(String albumName, String singer, String picPath) {
        this.albumName = albumName;
        this.singer = singer;
        this.picPath = picPath;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }


    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
