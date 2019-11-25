package com.music.fm544.bean;



public class Album {
    private int imgUrl;
    private String albumName;
    private String singer;


    public Album(int imgUrl, String albumName, String singer) {
        this.imgUrl = imgUrl;
        this.albumName = albumName;
        this.singer = singer;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
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
