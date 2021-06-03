package com.example.mywechat.ui.contacts;

import android.graphics.Bitmap;

public class Contact {
    private String nickname; // 昵称
    private Bitmap avatarIcon; // 头像

    public Contact(String nickname, Bitmap avatarIcon) {
        this.nickname = nickname;
        this.avatarIcon = avatarIcon;
    }

    public Bitmap getAvatarIcon() {
        return avatarIcon;
    }

    public String getNickname() {
        return nickname;
    }
}