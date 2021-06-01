package com.example.mywechat.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.api.ContactFindResponse;
import com.example.mywechat.ui.chatViewFragment.ChatBubble;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class NewFriendActivity extends AppCompatActivity {
    private ImageButton backToUserButton;
    private Button newFriendButton;
    private TextView friendNameText;
    private RecyclerView recyclerView;
    private LinkedList<ChatBubble> data;
    private NewFriendViewModel NfViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_friend);
        NfViewModel = new ViewModelProvider(this).get(NewFriendViewModel.class);

        backToUserButton = findViewById(R.id.backToUserButton);
        newFriendButton = findViewById(R.id.newFriendButton);
        friendNameText = findViewById(R.id.friendNameText);

        backToUserButton.setOnClickListener(v -> {
            finish();
        });

        newFriendButton.setOnClickListener(v -> {
            findFriend();
        });
    }

    private void findFriend() {
        String friendToFind = (String) friendNameText.getText();
        NfViewModel.contactFind(friendToFind);
        MutableLiveData<ContactFindResponse> liveData = NfViewModel.getLiveData();
        ContactFindResponse response = liveData.getValue();
        ArrayList<String> usernames = response.component2();
        ArrayList<String> usericon = response.component3();
        // TODO recyclerView.add
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
