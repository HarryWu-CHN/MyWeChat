package com.example.mywechat.ui.Group;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.FriendActivity;
import com.example.mywechat.R;
import com.example.mywechat.Util.FileUtil;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.ui.contacts.Contact;
import com.example.mywechat.viewmodel.GroupViewModel;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GroupActivity extends AppCompatActivity  {
    private ImageButton backToUserButton;
    private Button sendMessageButton;
    private Button quitButton;

    private final int spanCount = 5;
    private ArrayList<Contact> contacts;
    private AppendAdapter adapter;
    private RecyclerView memberRecyclerView;

    private String groupId;
    private String groupName;

    private GroupViewModel groupViewModel;

    private final int REQUEST_CODE_INVITE = 100;
    private final int REQUEST_CODE_FRIEND = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_group);

        initButtons();
        initRecyclerView();

        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        groupName = intent.getStringExtra("groupName");
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        groupViewModel.getMembers(groupId);
        groupViewModel.getGroupMembers().observe(this, response -> {
            if (response == null) {
                return;
            }

            List<String> memberUserNames = response.getMemberUsernames();
            List<String> memberNickNames = response.getMemberNicknames();
            List<String> memberIcons = response.getMemberIcons();

            contacts = new ArrayList<>();
            for (int i = 0; i < memberUserNames.size(); i++) {
                String memberName = memberUserNames.get(i);
                String memberNickName = memberNickNames.get(i);
                String memberIcon = memberIcons.get(i);

                FriendRecord friendRecord = LitePal.where("friendName = ?", memberName).findFirst(FriendRecord.class);
                if (friendRecord == null || friendRecord.getIconPath() == null) {
                    contacts.add(new Contact(memberName, memberNickName));
                    friendRecord = new FriendRecord(memberName, memberNickName);
                    getIconAndSave(friendRecord, memberIcon, i);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(friendRecord.getIconPath());
                    contacts.add(new Contact(memberName, memberNickName, bitmap));
                }
            }
            adapter.setMembers(contacts);
        });
    }

    private void initButtons() {
        backToUserButton = findViewById(R.id.backToUserButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        quitButton = findViewById(R.id.quitButton);

        backToUserButton.setOnClickListener(v -> {
            finish();
        });
        sendMessageButton.setOnClickListener(v -> {
            sendMessageBtnClicked();
        });
        quitButton.setOnClickListener(v -> {
            quitGroupBtnClicked();
        });
    }

    private void sendMessageBtnClicked() {
        Intent intent = new Intent(this, GroupChat.class);
        Bundle bundle = new Bundle();
        bundle.putString("groupId", groupId);
        bundle.putString("groupName", groupName);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void quitGroupBtnClicked() {
        groupViewModel.groupExit(groupId);
        finish();
    }

    private void initRecyclerView() {
        memberRecyclerView = findViewById(R.id.memberRecyclerView);
        memberRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new AppendAdapter();
        adapter.setOnItemClickListener((view, position) -> {
            if (position == adapter.getItemCount() - 1) {
                Intent intent = new Intent(this, NewGroupActivity.class);
                intent.putExtra("type", "invite");
                intent.putExtra("id", groupId);
                startActivityForResult(intent, REQUEST_CODE_INVITE);
            } else {
                if (contacts.get(position).getAvatarIcon() == null) {
                    return;
                }

                ByteArrayOutputStream iconBytes = new ByteArrayOutputStream();
                contacts.get(position).getAvatarIcon().compress(Bitmap.CompressFormat.JPEG, 100, iconBytes);
                Intent intent = new Intent(this, FriendActivity.class);
                intent.putExtra("userName", contacts.get(position).getUserName());
                intent.putExtra("nickName", contacts.get(position).getNickName());
                intent.putExtra("avatarBytes", iconBytes.toByteArray());
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
                groupViewModel.getMembers(groupId);
                break;
            case REQUEST_CODE_FRIEND:
                // ignored
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Pair<Integer, Bitmap> updatePair = (Pair<Integer, Bitmap>) msg.obj;
                    contacts.get(updatePair.first).setAvatarIcon(updatePair.second);
                    adapter.setMembers(contacts);
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

    public void getIconAndSave(final FriendRecord friendRecord, final String friendIcon, final int index) {
        //开启一个线程用于联网
        new Thread(() -> {
            int arg1 = 0;
            String path = "http://8.140.133.34:7262/" + friendIcon;
            Bitmap bitmap = null;
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
            File file = FileUtil.SaveBitmap2Png(bitmap);
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = arg1;
            msg.obj = new Pair<>(index, bitmap);
            handler.sendMessage(msg);
            friendRecord.setIconPath(file.getAbsolutePath());
            friendRecord.save();
        }).start();
    }
}
