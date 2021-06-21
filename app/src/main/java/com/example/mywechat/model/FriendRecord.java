package com.example.mywechat.model;

import android.util.Pair;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class FriendRecord extends LitePalSupport {
    @Column(unique = true)
    private String userName;
    private List<String> friendsName = new ArrayList<>();
    private List<String> friendsIcon = new ArrayList<>();

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public void setFriendsName(List<String> friendsName) {
        this.friendsName = friendsName;
    }

    public void setFriendsIcon(List<String> friendsIcon) {
        this.friendsIcon = friendsIcon;
    }

    public List<String> getFriendsName() {
        return friendsName;
    }

    public List<String> getFriendsIcon() {
        return friendsIcon;
    }
}
