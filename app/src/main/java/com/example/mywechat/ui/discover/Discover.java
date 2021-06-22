package com.example.mywechat.ui.discover;

import android.graphics.Bitmap;

import com.example.mywechat.ui.comment.Comment;

import java.util.ArrayList;

public class Discover {
    private String discoverId;
    private String discoverType;
    private String username;
    private String nickname;
    private String text;
    private String publishedTime;
    private String videoUrl;
    private ArrayList<String> thumbUsers;
    private ArrayList<Comment> comments;

    private Bitmap avatarIcon;
    private ArrayList<Bitmap> images;

    public Discover() {

    }

    public Discover(String discoverId, String discoverType, String nickname, String text,
                    String publishedTime, ArrayList<String> thumbUsers, ArrayList<Comment> comments) {
        this.discoverId = discoverId;
        this.discoverType = discoverType;
        this.nickname = nickname;
        this.text = text;
        this.publishedTime = publishedTime;
        this.thumbUsers = thumbUsers;
        this.comments = comments;
    }

    public void setDiscoverId(String discoverId) {
        this.discoverId = discoverId;
    }

    public void setDiscoverType(String discoverType) {
        this.discoverType = discoverType;
    }

    public void setAvatarIcon(Bitmap avatarIcon) {
        this.avatarIcon = avatarIcon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setImages(ArrayList<Bitmap> images) {
        this.images = images;
    }

    public void setThumbUsers(ArrayList<String> thumbUsers) {
        this.thumbUsers = thumbUsers;
    }

    public String getDiscoverId() {
        return discoverId;
    }

    public String getDiscoverType() {
        return discoverType;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public Bitmap getAvatarIcon() {
        return avatarIcon;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public int getImageCount() {
        return images.size();
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public String getText() {
        return text;
    }

    public ArrayList<String> getThumbUsers() {
        return thumbUsers;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}

