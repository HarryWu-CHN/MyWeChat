package com.example.mywechat.Activities.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.mywechat.R;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends AppCompatActivity {
    private TextView topName;
    private String sendTo;

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
    }

    public String getSendTo() {
        return sendTo;
    }
}