package com.example.mywechat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mywechat.Activities.Chat.ChatActivity;

public class FriendActivity extends AppCompatActivity {
    private ImageView friendAvatar;
    private TextView friendNickName;
    private TextView friendUserName;
    private Button sendMessageButton;
    private ImageButton backToContactButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friend);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String nickName = intent.getStringExtra("nickName");
        byte[] avatarBytes = intent.getByteArrayExtra("avatarBytes");

        friendAvatar = findViewById(R.id.friend_avatar);
        friendNickName = findViewById(R.id.friendNickName);
        friendUserName = findViewById(R.id.friendUserName);
        friendUserName.setText(userName);
        friendNickName.setText(nickName);
        Bitmap avatarBm = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
        friendAvatar.setImageBitmap(avatarBm);

        // TODO: 向联系人发起聊天
        sendMessageButton = findViewById(R.id.sendMessageButton);
        sendMessageButton.setOnClickListener(v -> {
            Intent it = new Intent(this, ChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", userName);
            bundle.putString("nickname", nickName);
            bundle.putByteArray("friendAvatarBytes", avatarBytes);
            it.putExtras(bundle);
            startActivity(it);
        });

        backToContactButton = findViewById(R.id.backToContactButton);
        backToContactButton.setOnClickListener(v -> {
            finish();
        });
    }
}
