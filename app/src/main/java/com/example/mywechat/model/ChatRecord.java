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
    private List<String> times = new ArrayList<>();
    private List<Boolean> isUser = new ArrayList<>();

    public ChatRecord(String userName, String friendName) {
        this.userName = userName;
        this.friendName = friendName;
    }

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

    public List<Boolean> getIsUser() {
        return isUser;
    }

    public void setIsUser(List<Boolean> isUser) {
        this.isUser = isUser;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public void addAllYouNeed(String msg, String msgType, String time, Boolean isuser) {
        msgs.add(msg);
        msgTypes.add(msgType);
        times.add(time);
        isUser.add(isuser);
    }
}
