package com.example.mywechat.ui.Group;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
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

import org.litepal.LitePal;

import java.util.LinkedList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewGroupActivity extends AppCompatActivity {
    private ImageButton backToUserButton;
    private Button inviteButton;
    private RecyclerView recyclerView;
    private InviteAdapter adapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_group);
        username = ((App)getApplication()).getUsername();

        backToUserButton = findViewById(R.id.backToUserButton);
        backToUserButton.setOnClickListener(v -> {
            finish();
        });

        inviteButton = findViewById(R.id.invitebutton);
        inviteButton.setOnClickListener(v -> {

        });

        recyclerView = findViewById(R.id.inviteRecyclerView);
        LinkedList<Contact> contacts = new LinkedList<>();
        // 从数据库中获取联系人信息
        UserInfo userInfo = LitePal.where("username = ?", username).findFirst(UserInfo.class);
        for (String friendName : userInfo.getFriendNames()) {
            FriendRecord friendRecord = LitePal.where("friendName = ?", friendName).findFirst(FriendRecord.class);
            contacts.add(new Contact( friendName, FileUtil.BytesToBitmap(friendRecord.getFriendIcon()) ));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new InviteAdapter(contacts);
        recyclerView.setAdapter(adapter);
    }
}
