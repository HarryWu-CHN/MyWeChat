package com.example.mywechat.ui.Group;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.FriendActivity;
import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.Contact;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity  {
    private ImageButton backToUserButton;
    private Button sendMessageButton;
    private Button clearButton;
    private Button quitButton;

    private final int spanCount = 5;
    private AppendAdapter adapter;
    private RecyclerView memberRecyclerView;

    private final int REQUEST_CODE_INVITE = 100;
    private final int REQUEST_CODE_FRIEND = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_group);

        // TODO: 拉取群聊成员列表

        initButtons();
        initRecyclerView();
    }

    private void initButtons() {
        backToUserButton = findViewById(R.id.backToUserButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        clearButton = findViewById(R.id.clearButton);
        quitButton = findViewById(R.id.quitButton);

        backToUserButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void initRecyclerView() {
        memberRecyclerView = findViewById(R.id.memberRecyclerView);
        memberRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new AppendAdapter();
        adapter.setOnItemClickListener((view, position) -> {
            if (position == adapter.getItemCount() - 1) {
                Intent intent = new Intent(this, NewGroupActivity.class);
                startActivityForResult(intent, REQUEST_CODE_INVITE);
            } else {
                Intent intent = new Intent(this, FriendActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FRIEND);
            }
        });
        memberRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_INVITE:
                // TODO: 更新群聊成员列表
                ArrayList<Contact> members = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    members.add(new Contact(String.valueOf(i), null));
                }
                adapter.setMembers(members);
                break;
            case REQUEST_CODE_FRIEND:
                // ignored
                break;
        }
    }
}
