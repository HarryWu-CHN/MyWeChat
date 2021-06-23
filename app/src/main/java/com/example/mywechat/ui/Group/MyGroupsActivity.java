package com.example.mywechat.ui.Group;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.viewmodel.GroupViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyGroupsActivity extends AppCompatActivity  {
    private Button backToContactButton;

    private ArrayList<Group> groups;
    private GroupAdapter adapter;
    private RecyclerView myGroupsRecyclerView;

    private GroupViewModel groupViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);
        getSupportActionBar().hide();
        ((LinearLayout) findViewById(R.id.myGroupTitle)).setMinimumHeight(getSupportActionBar().getHeight());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myGroupsRecyclerView = findViewById(R.id.myGroupsRecyclerView);
        myGroupsRecyclerView.setLayoutManager(linearLayoutManager);
        myGroupsRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new GroupAdapter();
        myGroupsRecyclerView.setAdapter(adapter);

        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        groupViewModel.getGroupsData().observe(this, response ->{
            if (response == null) {
                return;
            }

            List<String> groupIds = response.getGroupIds();
            List<String> groupNames = response.getGroupNames();
            List<String> creatorIcons = response.getCreatorIcons();
            ArrayList<Pair<String, String>> download = new ArrayList<>();

            if (groupIds == null || groupIds.size() == 0) {
                adapter.setGroups(groups);
                return;
            }
            groups = new ArrayList<>();
            for (int i = 0; i < groupIds.size(); i++) {
                groups.add(new Group(groupIds.get(i), groupNames.get(i)));
                download.add(new Pair<>(groupIds.get(i), creatorIcons.get(i)));
            }

            for (Pair<String, String> downloadPair : download) {
                getIcon(downloadPair);
            }
        });

        backToContactButton = findViewById(R.id.backToContactButton);
        backToContactButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupViewModel.getGroups();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Pair<String, Bitmap> updatePair = (Pair<String, Bitmap>) msg.obj;
                    for (Group group : groups) {
                        if (group.getGroupId().equals(updatePair.first)) {
                            group.setCreatorIcon(updatePair.second);
                        }
                    }
                    adapter.setGroups(groups);
                    break;
                case 1:
                    Log.d("InternetImageView", "NETWORK_ERROR");
                    break;
                case 2:
                    Log.d("InternetImageView", "SERVER_ERROR");
                    break;
                default:
                    break;
            }

        }
    };

    public void getIcon(final Pair<String, String> downloadPair) {
        //开启一个线程用于联网
        new Thread(() -> {
            int arg1 = 0;
            String path = "http://8.140.133.34:7262/" + downloadPair.second;
            Bitmap bitmap = null;
            try {
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } else {
                    arg1 = 1;
                    Log.d("HttpError", "下载图片失败");
                }
            } catch (IOException e) {
                arg1 = 2;
                e.printStackTrace();
            }
            if (bitmap == null) return;
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = arg1;
            msg.obj = new Pair<>(downloadPair.first, bitmap);
            handler.sendMessage(msg);
        }).start();
    }
}
