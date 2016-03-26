package com.example.chengen.mupetune;

import android.graphics.drawable.Drawable;

public class SongDataProvider {
    private Drawable songImage;
    private String songName;
    private String songArtist;

    public SongDataProvider(Drawable songImage, String songName, String songArtist) {
        this.songImage = songImage;
        this.songName = songName;
        this.songArtist = songArtist;
    }

    public Drawable getSongImage() {
        return songImage;
    }

    public void setSongImage(Drawable songImage) {
        this.songImage = songImage;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }
}
