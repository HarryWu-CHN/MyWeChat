package com.example.mywechat.model;

import android.util.Pair;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class ChatRecord extends LitePalSupport {
    private String userName;
    private String friendName;
    private List<String> msgs = new ArrayList<>();
    private List<String> msgTypes = new ArrayList<>();

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setMsgs(List<String> msgs) {
        this.msgs = msgs;
    }

    public void setMsgTypes(List<String> msgTypes) {
        this.msgTypes = msgTypes;
    }

    public List<String> getMsgs() {
        return msgs;
    }

    public List<String> getMsgTypes() {
        return msgTypes;
    }
}
