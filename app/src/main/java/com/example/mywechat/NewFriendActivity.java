package com.example.mywechat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class NewFriendActivity extends AppCompatActivity {
    private ImageButton backToUserButton;
    private Button newFriendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_friend);

        backToUserButton = findViewById(R.id.backToUserButton);
        newFriendButton = findViewById(R.id.newFriendButton);

        backToUserButton.setOnClickListener(v -> {
            finish();
        });

        newFriendButton.setOnClickListener(v -> {
            setAddFriendView();
        });
    }

    private void setAddFriendView() {
        setContentView(R.layout.fragment_new_friend);

        ImageView newFriendAvatar = findViewById(R.id.newFriendAvatar);
        TextView newFriendNickName = findViewById(R.id.newFriendNickName);
        TextView newFriendUserName = findViewById(R.id.newFriendUserName);
        Button addNewFriendButton = findViewById(R.id.addNewFriendButton);

        // TODO: 初始化朋友信息

        addNewFriendButton.setOnClickListener(v -> {
            // TODO: 添加好友
        });
    }
}
