package com.example.kicinamartin.songsplayer;

import android.widget.ImageView;

public class FeedEntry {

    private String title;
    private String name;
    private String releaseDate;
    private String imageURL;
    private ImageView image;
    private String views;
    private String songURL;


    public String getSongURL() {
        return songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public ImageView getImage(){
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getTitle(){
        return title;
    }

    public String getName() {
        return name;
    }


    public String getReleaseDate() {
        return releaseDate;
    }


    public String getImageURL() {
        return imageURL;
    }


    public String getViews() {
        return views;
    }

    @Override
    public String toString() {
        return "Title= " + title + '\n' +
                "name='" + name + '\n' +
                ", releaseDate='" + releaseDate + '\n' +
                ", views=" + views + '\n' +
                "url=" + imageURL;
    }
}
