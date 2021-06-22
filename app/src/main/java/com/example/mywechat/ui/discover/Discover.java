package com.example.mywechat.ui.discover;

import android.graphics.Bitmap;

import com.example.mywechat.ui.comment.Comment;

import java.util.ArrayList;

public class Discover {
    private String discoverId;
    private String discoverType;
    private int avatarIcon; //头像
    private String nickname; //昵称
    private String text; // 文字
    private String publishedTime; // 发布时间
    private String videoUrl;
    private ArrayList<Bitmap> images; // 图片
    private ArrayList<String> thumbUsers; // 点赞
    private ArrayList<Comment> comments; // 评论

    public Discover() {

    }

    public Discover(String discoverId, String discoverType, int avatarIcon, String nickname, String text,
                    String publishedTime, ArrayList<String> thumbUsers, ArrayList<Comment> comments) {
        this.discoverId = discoverId;
        this.discoverType = discoverType;
        this.nickname = nickname;
        this.avatarIcon = avatarIcon;
        this.text = text;
        this.publishedTime = publishedTime;
        this.thumbUsers = thumbUsers;
        this.comments = comments;
    }

//    public Discover(String discoverId, String nickname, int avatarIcon, String text, String publishedTime,
//                    ArrayList<Bitmap> images, ArrayList<String> thumbUsers, ArrayList<Comment> comments) {
//        this.discoverId = discoverId;
//        this.nickname = nickname;
//        this.avatarIcon = avatarIcon;
//        this.text = text;
//        this.publishedTime = publishedTime;
//        this.images = images;
//        this.thumbUsers = thumbUsers;
//        this.comments = comments;
//    }

    public void setDiscoverId(String discoverId) {
        this.discoverId = discoverId;
    }

    public void setDiscoverType(String discoverType) {
        this.discoverType = discoverType;
    }

    public void setAvatarIcon(int avatarIcon) {
        this.avatarIcon = avatarIcon;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPublishedTime(String publishedTime) {
        this.publishedTime = publishedTime;
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

    public String getNickname() {
        return nickname;
    }

    public int getAvatarIcon() {
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

