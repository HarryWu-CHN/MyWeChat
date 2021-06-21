package com.example.mywechat.ui.discover;

import android.graphics.Bitmap;

import com.example.mywechat.ui.comment.Comment;

import java.util.ArrayList;

public class Discover {
    private String discoverId;
    private int avatarIcon; //头像
    private String nickname; //昵称
    private String text; // 文字
    private String publishedTime; // 发布时间
    private ArrayList<Bitmap> images; // 图片
    private ArrayList<String> thumbUsers; // 点赞
    private ArrayList<Comment> comments; // 评论

    public Discover(String discoverId, String nickname, int avatarIcon, String text, String publishedTime,
                    ArrayList<Bitmap> images, ArrayList<String> thumbUsers, ArrayList<Comment> comments) {
        this.discoverId = discoverId;
        this.nickname = nickname;
        this.avatarIcon = avatarIcon;
        this.text = text;
        this.publishedTime = publishedTime;
        this.images = images;
        this.thumbUsers = thumbUsers;
        this.comments = comments;
    }

    public String getDiscoverId() {
        return discoverId;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAvatarIcon() {
        return avatarIcon;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public String getText() {
        return text;
    }

    public int getImageCount() {
        return images.size();
    }

    public ArrayList<String> getThumbUsers() {
        return thumbUsers;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}

