package com.example.mywechat.ui.chatViewFragment;

public class ChatBubble {
    private final String time; // 发送时间
    private final String content; // 发送内容
    private final boolean speaker; // 发送者

    public ChatBubble(String time, String content, boolean speaker) {
        this.time = time;
        this.content = content;
        this.speaker = speaker;
    }

    public String getTime() {
        return time;
    }
    public String getContent() {
        return content;
    }
    public boolean isSpeaker() {
        return speaker;
    }
}
