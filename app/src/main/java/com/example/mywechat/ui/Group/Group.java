package com.example.mywechat.ui.Group;

import android.graphics.Bitmap;

public class Group {
    private String groupId;
    private String groupName;
    private Bitmap creatorIcon;

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Bitmap getCreatorIcon() {
        return creatorIcon;
    }

    public void setCreatorIcon(Bitmap creatorIcon) {
        this.creatorIcon = creatorIcon;
    }
}
