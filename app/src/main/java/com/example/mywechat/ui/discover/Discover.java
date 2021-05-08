package com.example.mywechat.ui.discover;

import java.util.ArrayList;

public class Discover {
    private int avatarIcon; //头像
    private String nickname; //昵称
    private String text; // 文字
    private String publishedTime; // 发布时间
    private ArrayList<Integer> images; // 图片

    public Discover(String nickname, int avatarIcon, String text, String publishedTime, ArrayList<Integer> images) {
        this.nickname = nickname;
        this.avatarIcon = avatarIcon;
        this.text = text;
        this.publishedTime = publishedTime;
        this.images = images;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAvatarIcon() {
        return avatarIcon;
    }

    public ArrayList<Integer> getImages() {
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
}

