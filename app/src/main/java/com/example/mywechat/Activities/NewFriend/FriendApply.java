package com.example.mywechat.Activities.NewFriend;

public class FriendApply {
    private String friendName;
    private int friendAvatar;

    public FriendApply(String friendName, int friendAvatar) {
        this.friendName = friendName;
        this.friendAvatar = friendAvatar;
    }

    public String getFriendName() {
        return friendName;
    }

    public int getFriendAvatar() {
        return friendAvatar;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public void setFriendAvatar(int friendAvatar) {
        this.friendAvatar = friendAvatar;
    }
}
