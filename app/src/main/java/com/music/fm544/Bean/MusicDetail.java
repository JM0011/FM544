package com.music.fm544.Bean;



public class MusicDetail {
    private String music_name;
    private String music_album;
    private String music_author;
    private Integer music_time;
    private String music_pic_path;
    private String music_path;
    private Integer music_size;

    public MusicDetail() {
    }

    public MusicDetail(String music_name, String music_album, String music_author, Integer music_time, String music_pic_path, String music_path, Integer music_size) {
        this.music_name = music_name;
        this.music_album = music_album;
        this.music_author = music_author;
        this.music_time = music_time;
        this.music_pic_path = music_pic_path;
        this.music_path = music_path;
        this.music_size = music_size;
    }

    public String getMusic_name() {
        return music_name;
    }

    public void setMusic_name(String music_name) {
        this.music_name = music_name;
    }

    public String getMusic_album() {
        return music_album;
    }

    public void setMusic_album(String music_album) {
        this.music_album = music_album;
    }

    public String getMusic_author() {
        return music_author;
    }

    public void setMusic_author(String music_author) {
        this.music_author = music_author;
    }

    public Integer getMusic_time() {
        return music_time;
    }

    public void setMusic_time(Integer music_time) {
        this.music_time = music_time;
    }

    public String getMusic_pic_path() {
        return music_pic_path;
    }

    public void setMusic_pic_path(String music_pic_path) {
        this.music_pic_path = music_pic_path;
    }

    public String getMusic_path() {
        return music_path;
    }

    public void setMusic_path(String music_path) {
        this.music_path = music_path;
    }

    public Integer getMusic_size() {
        return music_size;
    }

    public void setMusic_size(Integer music_size) {
        this.music_size = music_size;
    }
}
