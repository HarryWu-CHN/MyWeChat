package com.example.mywechat.model;

import android.util.Pair;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class ChatRecord extends LitePalSupport {
    private String userName;
    private String friendName;
    private List<Pair<String, MessageType>> messages = new ArrayList<>();

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setMessages(List<Pair<String, MessageType>> messages) {
        this.messages = messages;
    }

    public String getUserName() {
        return userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public List<Pair<String, MessageType>> getMessages() {
        return messages;
    }
}
