package com.example.mywechat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FriendActivity extends AppCompatActivity {
    private ImageView friendAvatar;
    private TextView friendNickName;
    private TextView friendUserName;
    private Button sendMessageButton;
    private Button backToContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        friendAvatar = findViewById(R.id.friend_avatar);
        friendNickName = findViewById(R.id.friendNickName);
        friendUserName = findViewById(R.id.friendUserName);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        backToContactButton = findViewById(R.id.backToContactButton);

        backToContactButton.setOnClickListener(v -> {
            finish();
        });
    }
}
