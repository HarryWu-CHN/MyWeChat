package com.example.mywechat.ui.contacts;

import android.graphics.Bitmap;

public class Contact {
    private String userName; // 用户名
    private String nickName; // 昵称
    private Bitmap avatarIcon; // 头像

    public Contact() {

    }

    public Contact(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
        this.avatarIcon = null;
    }

    public Contact(String userName, String nickName, Bitmap avatarIcon) {
        this.userName = userName;
        this.nickName = nickName;
        this.avatarIcon = avatarIcon;
    }

    public void setAvatarIcon(Bitmap avatarIcon) {
        this.avatarIcon = avatarIcon;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public Bitmap getAvatarIcon() {
        return avatarIcon;
    }
}