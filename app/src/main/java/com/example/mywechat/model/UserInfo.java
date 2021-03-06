package com.example.mywechat.model;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class UserInfo extends LitePalSupport {
    @Column(unique = true)
    private String username;
    private String nickName;
    private String userIcon;
    private List<String> friendNames;

    public UserInfo(String username) {
        this.username = username;
        friendNames = new ArrayList<>();
    }

    public UserInfo(String username, List<String> friendNames) {
        this.username = username;
        this.friendNames = friendNames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getFriendNames() {
        return friendNames;
    }

    public void setFriendNames(List<String> friendNames) {
        this.friendNames = friendNames;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public void addFriend(String friendName) {
        friendNames.add(friendName);
    }
}
