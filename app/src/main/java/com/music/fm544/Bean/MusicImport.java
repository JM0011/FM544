package com.music.fm544.Bean;

/**
 * Created by jm on 2019/10/27/0027.
 */

public class MusicImport {

    private int imgId;
    private String songName;
    private String singer;
    private Boolean choose = false;

    public MusicImport(int imgId, String songName, String singer, Boolean choose) {
        this.imgId = imgId;
        this.songName = songName;
        this.singer = singer;
        this.choose = choose;
    }

    public int getImgId() {
        return imgId;
    }

    public String getSongName() {
        return songName;
    }

    public String getSinger() {
        return singer;
    }

    public Boolean getChoose() {
        return choose;
    }
}
