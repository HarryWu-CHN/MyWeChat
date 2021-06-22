package com.example.mywechat.model;

import android.util.Pair;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class FriendRecord extends LitePalSupport {
    @Column(unique = true)
    private String friendName;
    private String nickName;
    private String iconPath;

    public FriendRecord(String friendName, String nickName) {
        this.friendName = friendName;
        this.nickName = nickName;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
