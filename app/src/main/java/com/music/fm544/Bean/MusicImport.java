package com.music.fm544.Bean;

/**
 * 导入音乐对象
 */
public class MusicImport extends MusicPO{

    private Boolean choose = false;
    private Boolean isExist = false;

    public MusicImport() {
    }

    public MusicImport(MusicPO music, Boolean choose,Boolean isExist) {
        super(music.getId(),music.getMusic_name(),music.getMusic_album(),music.getMusic_author(),music.getMusic_time(),music.getMusic_pic_path(),music.getMusic_path(),music.getMusic_like_status(),music.isHighQuality());
        this.choose = choose;
        this.isExist = isExist;
    }

    public MusicImport(Integer id, String music_name, String music_album, String music_author, Integer music_time, String music_pic_path, String music_path, Integer music_like_status, Integer isHighQuality, Boolean choose,Boolean isExist) {
        super(id, music_name, music_album, music_author, music_time, music_pic_path, music_path, music_like_status, isHighQuality);
        this.choose = choose;
        this.isExist = isExist;
    }

    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getChoose() {
        return choose;
    }

    public void setChoose(Boolean choose) {
        this.choose = choose;
    }

}
