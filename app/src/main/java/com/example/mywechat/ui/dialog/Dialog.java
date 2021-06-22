package com.example.mywechat.ui.dialog;

public class Dialog {
    private final String username; // 名称
    private final String nickname; // 昵称
    private final String lastSpeak; //最后聊天内容
    private final String iconPath; // 头像
    private final String lastSpeakTime; //最后联络时间

    public Dialog(String username, String nickname, String iconPath, String lastSpeak, String lastSpeakTime) {
        this.username = username;
        this.nickname = nickname;
        this.iconPath = iconPath;
        this.lastSpeak = lastSpeak;
        this.lastSpeakTime = lastSpeakTime;
    }

    public String getUsername() {
        return username;
    }

    public String getIconPath() {
        return iconPath;
    }

    public String getLastSpeak() {
        return lastSpeak;
    }

    public String getLastSpeakTime() {
        return lastSpeakTime;
    }

    public String getNickname() {
        return nickname;
    }
}
