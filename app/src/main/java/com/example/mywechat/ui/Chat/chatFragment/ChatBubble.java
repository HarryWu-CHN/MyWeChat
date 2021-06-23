package com.example.mywechat.ui.Chat.chatFragment;

import android.graphics.Bitmap;

public class ChatBubble {
    private final String time; // 发送时间
    private final Object content; // 发送内容
    private final Bitmap icon;
    private final boolean isUser;
    private final String msgType;

    public ChatBubble(String time, String content, Bitmap icon, Boolean isUser, String msgType) {
        this.time = time;
        this.content = content;
        this.icon = icon;
        this.isUser = isUser;
        this.msgType = msgType;
    }

    public ChatBubble(String time, Bitmap bitmap, Bitmap icon, Boolean isUser, String msgType) {
        this.time = time;
        this.content = bitmap;
        this.icon = icon;
        this.isUser = isUser;
        this.msgType = msgType;
    }

    public String getTime() {
        return time;
    }
    public Object getContent() {
        return content;
    }
    public Bitmap getIcon() {
        return icon;
    }

    public boolean isUser() {
        return isUser;
    }

    public int getIntMsgType() {
        return Integer.parseInt(msgType);
    }

}
