package com.example.mywechat.model;

import org.litepal.crud.LitePalSupport;

public class DialogRecord extends LitePalSupport {
    private String uniqueName;
    private String nickName;
    private String userOrGroup;
    private String iconPath;
    private String lastSpeak;

    public DialogRecord(String uniqueName, String nickName, String userOrGroup, String iconPath, String lastSpeak) {
        this.uniqueName = uniqueName;
        this.nickName = nickName;
        this.userOrGroup = userOrGroup;
        this.iconPath = iconPath;
        this.lastSpeak = lastSpeak;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public String getLastSpeak() {
        return lastSpeak;
    }

    public void setLastSpeak(String lastSpeak) {
        this.lastSpeak = lastSpeak;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserOrGroup() {
        return userOrGroup;
    }

    public void setUserOrGroup(String userOrGroup) {
        this.userOrGroup = userOrGroup;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
}
