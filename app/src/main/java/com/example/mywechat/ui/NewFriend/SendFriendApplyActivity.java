package com.example.mywechat.ui.NewFriend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.mywechat.R;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SendFriendApplyActivity extends AppCompatActivity {
    private ImageButton backToNewFriendButton;
    private ImageView newFriendAvatar;
    private TextView newFriendNickName;
    private TextView newFriendUserName;
    private Button addNewFriendButton;
    private NewFriendViewModel NfViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_friend);
        getSupportActionBar().hide();

        NfViewModel = new ViewModelProvider(this)
                .get(NewFriendViewModel.class);

        Intent intent = getIntent();

        backToNewFriendButton = findViewById(R.id.backToNewFriendButton);
        newFriendAvatar = findViewById(R.id.newFriendAvatar);
        newFriendAvatar.setImageResource(R.drawable.avatar4);
        newFriendUserName = findViewById(R.id.newFriendNickName);
        addNewFriendButton = findViewById(R.id.addNewFriendButton);

        backToNewFriendButton.setOnClickListener(v -> {
            finish();
        });

        // TODO: 保存到本地后读取
        // newFriendAvatar.setImageResource();
        String friendUserName = intent.getStringExtra("friendUserName");
        newFriendUserName.setText(friendUserName);

        addNewFriendButton.setOnClickListener(v -> {
            NfViewModel.contactAdd(friendUserName);
            finish();
        });
    }
}
