package com.example.mywechat.ui.comment;

public class Comment {
    private String nickName;
    private String text;

    public Comment(String nickName, String text) {
        this.nickName = nickName;
        this.text = text;
    }

    public String getNickName() {
        return this.nickName;
    }

    public String getText() {
        return this.text;
    }
}

