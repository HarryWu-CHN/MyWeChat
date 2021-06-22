package com.example.mywechat.ui.Group;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.Contact;

import java.util.LinkedList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewGroupActivity extends AppCompatActivity {
    private ImageButton backToUserButton;
    private Button inviteButton;
    private RecyclerView recyclerView;
    private InviteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_group);

        backToUserButton = findViewById(R.id.backToUserButton);
        backToUserButton.setOnClickListener(v -> {
            finish();
        });

        inviteButton = findViewById(R.id.invitebutton);
        inviteButton.setOnClickListener(v -> {

        });

        recyclerView = findViewById(R.id.inviteRecyclerView);

        // TODO: 获取联系人并放进 contacts
        LinkedList<Contact> contacts = new LinkedList<>();
        contacts.add(new Contact("a", null));
        contacts.add(new Contact("b", null));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new InviteAdapter(contacts);
        recyclerView.setAdapter(adapter);
    }
}
