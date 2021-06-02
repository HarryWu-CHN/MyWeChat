package com.example.mywechat.Activities.NewFriend;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.InfoViewModel;
import com.example.mywechat.R;
import com.example.mywechat.api.ContactFindResponse;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

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
        NfViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
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
    }

    private void findFriend() {
        String friendToFind = (String) friendNameText.getText();
        NfViewModel.contactFind(friendToFind);
        MutableLiveData<ContactFindResponse> liveData = NfViewModel.getLiveData();
        ContactFindResponse response = liveData.getValue();
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> usericon = new ArrayList<>();
        if (response != null) {
            usernames = response.component2();
            usericon = response.component3();
        }
        Log.d("FindFriend", usernames.toString());
        Log.d("FindFriend Icon", usericon.toString());
//        recyclerView = findViewById(R.id.recyclerView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(new NewFriendAdapter(newFriends));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
    /**
    private final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // 通过Handler设置图片
            // 并正确处理 SUCCESS、NETWORK_ERROR、SERVER_ERROR 类型的消息
            // 提示：使用 setImageBitmap() 来将Bitmap对象显示到UI上
            switch (msg.what){
                case SUCCESS:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    setImageBitmap(bitmap);
                    break;
                case NETWORK_ERROR:
                    Log.d("InternetImageView", "NETWORK_ERROR");
                    break;
                case SERVER_ERROR:
                    Log.d("InternetImageView", "SERVER_ERROR");
                    break;
                default:
                    break;
            }

        }
    };

    public void setImageURL(final String path) {
        //开启一个线程用于联网
        new Thread(() -> {
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
                    // TODO
                    // 利用Message把图片发给Handler
                    // Message的obj成员变量可以用来传递对象
                    Message msg = new Message();
                    msg.what = SUCCESS;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                    // TODO 部分结束
                    inputStream.close();
                } else {
                    handler.sendEmptyMessage(SERVER_ERROR);
                }
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(NETWORK_ERROR);
            }
        }).start();
    }
     */
}
