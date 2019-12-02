package com.music.fm544.Bean;



public class MusicListItem extends MusicPO {
    private boolean isPlaying;

    public MusicListItem() {
    }

    public MusicListItem(Integer id, String music_name, String music_album, String music_author, Integer music_time, String music_pic_path, String music_path, Integer music_like_status, Integer isHighQuality, boolean isPlaying) {
        super(id, music_name, music_album, music_author, music_time, music_pic_path, music_path, music_like_status, isHighQuality);
        this.isPlaying = isPlaying;
    }

    public MusicListItem(MusicPO music, boolean isPlaying) {
        super(music.getId(),music.getMusic_name(),music.getMusic_album(),music.getMusic_author(),music.getMusic_time(),music.getMusic_pic_path(),music.getMusic_path(),music.getMusic_like_status(),music.isHighQuality());
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
