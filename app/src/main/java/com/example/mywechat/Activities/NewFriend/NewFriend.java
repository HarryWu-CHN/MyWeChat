package com.example.mywechat.Activities.NewFriend;

public class NewFriend {
    private String nickname; // 昵称
    private int avatarIcon; // 头像

    public NewFriend(String nickname, int avatarIcon) {
        this.nickname = nickname;
        this.avatarIcon = avatarIcon;
    }

    public int getAvatarIcon() {
        return avatarIcon;
    }

    public String getNickname() {
        return nickname;
    }
}