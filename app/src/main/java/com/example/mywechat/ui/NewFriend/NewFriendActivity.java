package com.example.mywechat.ui.NewFriend;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewFriendActivity extends AppCompatActivity {
    private ImageButton backToUserButton;
    private Button newFriendButton;
    private TextView friendNameText;
    private RecyclerView recyclerView;
    private LinkedList<NewFriend> newFriends;
    private NewFriendViewModel NfViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_friend);
        NfViewModel = new ViewModelProvider(this)
                .get(NewFriendViewModel.class);
        backToUserButton = findViewById(R.id.backToUserButton);
        newFriendButton = findViewById(R.id.newFriendButton);
        friendNameText = findViewById(R.id.friendNameText);

        backToUserButton.setOnClickListener(v -> {
            finish();
        });

        newFriendButton.setOnClickListener(v -> {
            findFriend();
        });

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        NfViewModel.getLiveData().observe(this, response -> {
            if (response == null) {
                return;
            }

            newFriends = new LinkedList<>();
            List<String> usernames = response.component3();
            for (String username : usernames) {
                NewFriend newFriend = new NewFriend(username);
                newFriends.add(newFriend);
            }
            List<String> usericon = response.component4();
            getIcons(usericon);
        });
    }

    private void findFriend() {
        String friendToFind = friendNameText.getText().toString();
        NfViewModel.contactFind(friendToFind);

        // TODO recyclerView.add
    }


    private Handler handler = new Handler(Looper.myLooper()) {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // ??????Handler????????????
            // ??????????????? SUCCESS???NETWORK_ERROR???SERVER_ERROR ???????????????
            // ??????????????? setImageBitmap() ??????Bitmap???????????????UI???
            switch (msg.what){
                case 0:
                    List<Bitmap> bitmaps = (List) msg.obj;
                    for (int i=0; i<bitmaps.size(); i++) {
                        newFriends.get(i).setAvatarIcon(bitmaps.get(i));
                    }
                    if (msg.arg1 == 1) {
                        Log.d("InternetIcon View", "NETWORK_ERROR");
                    } else if (msg.arg1 == 2) {
                        Log.d("InternetIcon View", "IOException");
                    }
                    recyclerView.setAdapter(new NewFriendAdapter(newFriends));
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

    public void getIcons(final List<String> usericon) {
        //??????????????????????????????
        new Thread(() -> {
            List<Bitmap> bitmaps = new ArrayList<>();
            int arg1 = 0;
            for (String path : usericon) {
                path = "http://8.140.133.34:7262/" + path;
                try {
                    //??????HTTP??????????????????
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    //???????????????
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        bitmaps.add(bitmap);
                        inputStream.close();
                    } else {
                        arg1 = 1;
                        bitmaps.add(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    bitmaps.add(null);
                    arg1 = 2;
                }
            }
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = arg1;
            msg.obj = bitmaps;
            handler.sendMessage(msg);
        }).start();
    }
}
