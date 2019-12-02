package com.music.fm544.Bean;


public class MusicPO {
    private Integer id;
    private String music_name;
    private String music_album;
    private String music_author;
    private Integer music_time;
    private String music_pic_path;
    private String music_path;
    private Integer music_like_status;
    private Integer isHighQuality;

    public MusicPO() {
    }

    public MusicPO(Integer id, String music_name, String music_album, String music_author, Integer music_time, String music_pic_path, String music_path, Integer music_like_status, Integer isHighQuality) {
        this.id = id;
        this.music_name = music_name;
        this.music_album = music_album;
        this.music_author = music_author;
        this.music_time = music_time;
        this.music_pic_path = music_pic_path;
        this.music_path = music_path;
        this.music_like_status = music_like_status;
        this.isHighQuality = isHighQuality;
    }

    public Integer isHighQuality() {
        return isHighQuality;
    }

    public void setHighQuality(Integer highQuality) {
        isHighQuality = highQuality;
    }

    public Integer getMusic_like_status() {
        return music_like_status;
    }

    public void setMusic_like_status(Integer music_like_status) {
        this.music_like_status = music_like_status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "MusicPO{" +
                "id=" + id +
                ", music_name='" + music_name + '\'' +
                ", music_album='" + music_album + '\'' +
                ", music_author='" + music_author + '\'' +
                ", music_time=" + music_time +
                ", music_pic_path='" + music_pic_path + '\'' +
                ", music_path='" + music_path + '\'' +
                ", music_like_status=" + music_like_status +
                '}';
    }
}
