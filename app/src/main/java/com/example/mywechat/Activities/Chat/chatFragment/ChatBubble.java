package com.example.mywechat.Activities.Chat.chatFragment;

public class ChatBubble {
    private final String time; // 发送时间
    private final String content; // 发送内容
    private final int icon;
    private final MsgType msgType;

    public ChatBubble(String time, String content, int icon, MsgType msgType) {
        this.time = time;
        this.content = content;
        this.icon = icon;
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
    public int getIntMsgType() {
        return msgType.ordinal();
    }
}
