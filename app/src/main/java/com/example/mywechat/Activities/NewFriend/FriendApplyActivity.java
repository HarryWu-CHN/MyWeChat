package com.example.mywechat.Activities.NewFriend;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.LinkedList;

public class FriendApplyActivity extends AppCompatActivity {
    private ImageButton backToApplyButton;
    private RecyclerView applyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fragment_friend_apply);

        backToApplyButton = findViewById(R.id.backToApplyButton);
        applyRecyclerView = findViewById(R.id.applyRecyclerView);

        backToApplyButton.setOnClickListener(v -> {
            finish();
        });

        // TODO: 索要好友申请
        setSampleApply();
    }

    private void setSampleApply() {
        LinkedList<FriendApply> friendApplies = new LinkedList<>();
        friendApplies.add(new FriendApply(getString(R.string.nickname1), R.drawable.avatar1));
        friendApplies.add(new FriendApply(getString(R.string.nickname2), R.drawable.avatar2));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        applyRecyclerView.setLayoutManager(linearLayoutManager);
        applyRecyclerView.setAdapter(new FriendApplyAdapter(friendApplies));
    }
}
