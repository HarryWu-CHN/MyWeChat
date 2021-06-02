package com.example.mywechat.model;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class FriendRecord extends LitePalSupport {
    private String userName;
    private List<String> friends = new ArrayList<>();

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getFriends() {
        return friends;
    }
}
