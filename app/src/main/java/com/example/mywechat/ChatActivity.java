package com.example.mywechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatActivity extends FragmentActivity {
    private TextView topName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_chat);
        Bundle bundle = getIntent().getExtras();
        String nickname = bundle.getString("nickname");
        int icon = bundle.getInt("icon");
        topName = findViewById(R.id.topName);
        topName.setText(nickname);
    }
}