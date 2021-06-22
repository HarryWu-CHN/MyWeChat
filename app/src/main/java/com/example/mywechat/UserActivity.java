package com.example.mywechat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mywechat.Util.FileUtil;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.ui.Group.NewGroupActivity;
import com.example.mywechat.Activities.NewFriend.NewFriendActivity;
import com.example.mywechat.viewmodel.NewFriendViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserActivity extends AppCompatActivity {
    private NewFriendViewModel nfViewModel;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dialog, R.id.navigation_contacts, R.id.navigation_discover, R.id.navigation_mine)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        username = getIntent().getStringExtra("username");
        ((App) getApplication()).setUsername(username);

        nfViewModel = new ViewModelProvider(this).get(NewFriendViewModel.class);
        nfViewModel.contactGet();
        nfViewModel.getContactsData().observe(this, response -> {
            if (response == null) {
                return;
            }
            List<String> friendNames = response.component1();
            List<String> friendIcons = response.component3();
            UserInfo userInfo = new UserInfo(username);
            for (int i=0; i<friendNames.size(); i++) {
                getIconAndSave(friendNames.get(i), friendIcons.get(i));
                userInfo.addFriend(friendNames.get(i));
            }
            userInfo.saveOrUpdate();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.newFriendItem:
                intent = new Intent(this, NewFriendActivity.class);
                startActivity(intent);
                break;
            case R.id.newGroupItem:
                intent = new Intent(this, NewGroupActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    FriendRecord friendRecord = (FriendRecord) msg.obj;
                    friendRecord.assignBaseObjId(1);
                    friendRecord.save();
                    break;
            }
        }
    };

    public void getIconAndSave(final String friendName, final String friendIcon) {
        //开启一个线程用于联网
        new Thread(() -> {
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
                    Log.d("HttpError", "下载图片失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) return;
            Message msg = new Message();
            msg.what = 0;
            msg.obj = new FriendRecord(friendName, FileUtil.Bitmap2Bytes(bitmap));
            handler.sendMessage(msg);
        }).start();
    }
}