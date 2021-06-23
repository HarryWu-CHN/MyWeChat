package com.example.mywechat.ui.discover.comment;

public class Comment {
    private String nickName;
    private String sendTo;
    private String text;

    public Comment(String nickName, String sendTo, String text) {
        this.nickName = nickName;
        this.sendTo = sendTo;
        this.text = text;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getSendTo() {
        return sendTo;
    }

    public String getText() {
        return this.text;
    }
}

