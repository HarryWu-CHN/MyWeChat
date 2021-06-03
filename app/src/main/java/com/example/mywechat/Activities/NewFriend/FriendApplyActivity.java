package com.example.mywechat.Activities.NewFriend;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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
public class FriendApplyActivity extends AppCompatActivity {
    private ImageButton backToApplyButton;
    private RecyclerView applyRecyclerView;

    private LinkedList<NewFriend> friendApplies;
    private NewFriendViewModel NfViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.fragment_friend_apply);

        NfViewModel = new ViewModelProvider(this)
                .get(NewFriendViewModel.class);

        backToApplyButton = findViewById(R.id.backToApplyButton);
        applyRecyclerView = findViewById(R.id.applyRecyclerView);

        backToApplyButton.setOnClickListener(v -> {
            finish();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        applyRecyclerView.setLayoutManager(linearLayoutManager);

        // TODO: 索要好友申请
        NfViewModel.contactWaited();

        NfViewModel.getAppliesData().observe(this, response -> {
            if (response == null) {
                return;
            }

            friendApplies = new LinkedList<>();

            List<String> usernames = response.component3();
            for (String username : usernames) {
                NewFriend newFriend = new NewFriend(username);
                friendApplies.add(newFriend);
            }
            List<String> usericon = response.component4();
            Log.d("FindFriend", usernames.toString());
            Log.d("FindFriend Icon", usericon.toString());
            getIcons(usericon);
        });

        //setSampleApply();
    }

    private void setSampleApply() {
        LinkedList<NewFriend> friendApplies = new LinkedList<>();
        friendApplies.add(new NewFriend(getString(R.string.nickname1)));
        friendApplies.add(new NewFriend(getString(R.string.nickname2)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        applyRecyclerView.setLayoutManager(linearLayoutManager);
        applyRecyclerView.setAdapter(new FriendApplyAdapter(friendApplies));
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // 通过Handler设置图片
            // 并正确处理 SUCCESS、NETWORK_ERROR、SERVER_ERROR 类型的消息
            // 提示：使用 setImageBitmap() 来将Bitmap对象显示到UI上
            switch (msg.what){
                case 0:
                    List<Bitmap> bitmaps = (List<Bitmap>) msg.obj;
                    for (int i=0; i<bitmaps.size(); i++) {
                        friendApplies.get(i).setAvatarIcon(bitmaps.get(i));
                    }
                    FriendApplyAdapter adapter = new FriendApplyAdapter(friendApplies);
                    applyRecyclerView.setAdapter(adapter);
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
        //开启一个线程用于联网
        new Thread(() -> {
            List<Bitmap> bitmaps = new ArrayList<>();
            int arg1 = 0;
            for (String path : usericon) {
                path = "http://8.140.133.34:7262/" + path;
                try {
                    //通过HTTP请求下载图片
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    //获取返回码
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // 利用Message把图片发给Handler
                        // Message的obj成员变量可以用来传递对象
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
