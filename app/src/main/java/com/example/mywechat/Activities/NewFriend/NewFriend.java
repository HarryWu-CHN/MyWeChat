package com.example.mywechat.Activities.NewFriend;

import android.graphics.Bitmap;

public class NewFriend {
    private String nickname; // 昵称
    private Bitmap avatarIcon; // 头像

    public NewFriend(String nickname, Bitmap avatarIcon) {
        this.nickname = nickname;
        this.avatarIcon = avatarIcon;
    }

    public NewFriend(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatarIcon(Bitmap avatarIcon) {
        this.avatarIcon = avatarIcon;
    }

    public Bitmap getAvatarIcon() {
        return avatarIcon;
    }

    public String getNickname() {
        return nickname;
    }
}