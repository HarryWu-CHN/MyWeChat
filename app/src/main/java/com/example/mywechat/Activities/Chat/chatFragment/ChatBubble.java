package com.example.mywechat.Activities.Chat.chatFragment;

import com.example.mywechat.model.MessageType;

public class ChatBubble {
    private final String time; // 发送时间
    private final String content; // 发送内容
    private final int icon;
    private final boolean isUser;
    private final String msgType;

    public ChatBubble(String time, String content, int icon, boolean isUser, String msgType) {
        this.time = time;
        this.content = content;
        this.icon = icon;
        this.isUser = isUser;
        this.msgType = msgType;
    }

    public String getTime() {
        return time;
    }
    public String getContent() {
        return content;
    }
    public int getIcon() {
        return icon;
    }

    public boolean isUser() {
        return isUser;
    }

    public int getIntMsgType() {
        return Integer.parseInt(msgType);
    }
}
