package com.music.fm544.Bean;


import java.io.Serializable;

public class Singer implements Serializable{
    private String singerName;
    private String musicNum;

    public Singer() {
    }

    public Singer(String singerName, String musicNum) {
        this.singerName = singerName;
        this.musicNum = musicNum;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getMusicNum() {
        return musicNum;
    }

    public void setMusicNum(String musicNum) {
        this.musicNum = musicNum;
    }
}
