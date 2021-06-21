package com.example.mywechat.model;

import android.util.Pair;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class FriendRecord extends LitePalSupport {
    @Column(unique = true)
    private String friendName;
    private byte[] friendIcon;

    public FriendRecord(String friendName, byte[] friendIcon) {
        this.friendName = friendName;
        this.friendIcon = friendIcon;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public byte[] getFriendIcon() {
        return friendIcon;
    }

    public void setFriendIcon(byte[] friendIcon) {
        this.friendIcon = friendIcon;
    }
}
