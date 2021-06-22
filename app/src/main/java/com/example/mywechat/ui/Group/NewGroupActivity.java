package com.example.mywechat.ui.Group;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.Util.FileUtil;
import com.example.mywechat.model.ChatRecord;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.ui.contacts.Contact;
import com.example.mywechat.viewmodel.GroupViewModel;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewGroupActivity extends AppCompatActivity {
    private ImageButton backToUserButton;
    private Button inviteButton;
    private RecyclerView recyclerView;
    private InviteAdapter adapter;
    private LinkedList<Contact> contacts;
    private String username;
    private List<String> inviteList;
    private GroupViewModel groupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_group);

        Intent intent = getIntent();

        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        username = ((App)getApplication()).getUsername();

        backToUserButton = findViewById(R.id.backToUserButton);
        backToUserButton.setOnClickListener(v -> {
            finish();
        });

        inviteButton = findViewById(R.id.invitebutton);
        inviteList = new ArrayList<>();
        inviteButton.setOnClickListener(v -> {
            Map<Integer, Boolean> mp = adapter.getMap();
            for (int i = 0; i < mp.size(); i++) {
                if (mp.get(i)) {
                    inviteList.add(contacts.get(i).getUserName());
                }
            }
            switch (intent.getStringExtra("type")) {
                case "create":
                    groupViewModel.groupCreate(null, inviteList);
                    break;
                case "invite":
                    for (String userName : inviteList) {
                        groupViewModel.groupAdd(intent.getStringExtra("id"), userName);
                    }
                    break;
            }
            finish();
        });

        recyclerView = findViewById(R.id.inviteRecyclerView);
        contacts = new LinkedList<>();
        // 从数据库中获取联系人信息
        UserInfo userInfo = LitePal.where("username = ?", username).findFirst(UserInfo.class);
        for (String friendName : userInfo.getFriendNames()) {
            FriendRecord friendRecord = LitePal.where("friendName = ?", friendName).findFirst(FriendRecord.class);
            if (friendRecord == null) continue;
            Bitmap bitmap = BitmapFactory.decodeFile(friendRecord.getIconPath());
            // TODO: 通过username nickname icon 重新设置 contact
            contacts.add(new Contact( friendName, friendName, bitmap ));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new InviteAdapter(contacts);
        recyclerView.setAdapter(adapter);
    }
}
