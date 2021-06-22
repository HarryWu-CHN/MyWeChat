package com.example.mywechat.Activities.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mywechat.R;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {
    private TextView topName;
    private String sendTo;
    private Bitmap friendBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        String nickname = bundle.getString("nickname");
        topName = findViewById(R.id.topName);
        topName.setText(nickname);
        sendTo = bundle.getString("username");
        //byte[] avatarBytes = bundle.getByteArray("friendAvatarBytes");
        //friendBitmap = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
    }

    public String getSendTo() {
        return sendTo;
    }
    public Bitmap getFriendBitmap() {
        return friendBitmap;
    }
}