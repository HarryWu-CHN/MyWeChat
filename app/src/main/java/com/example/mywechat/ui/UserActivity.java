package com.example.mywechat.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mywechat.R;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.ui.Group.NewGroupActivity;
import com.example.mywechat.ui.NewFriend.NewFriendActivity;
import com.example.mywechat.viewmodel.NewFriendViewModel;
import com.example.mywechat.viewmodel.UserInfoViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserActivity extends AppCompatActivity {
    private NewFriendViewModel nfViewModel;
    private UserInfoViewModel userInfoViewModel;
    private String username = "";
    private String password = "";

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
        password = getIntent().getStringExtra("password");
//        ((App) getApplication()).setUsername(username);

        userInfoViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);

        userInfoViewModel.openReLogin(username, password);

        userInfoViewModel.userGet(username);
        userInfoViewModel.getLiveData().observe(this, response -> {
            if (response == null) return;
            UserInfo userInfo = LitePal.where("username = ?", username).findFirst(UserInfo.class);
            if (userInfo == null) userInfo = new UserInfo(username);
            else {
                if (userInfo.getUserIcon() != null) {
                    File oldFile = new File(userInfo.getUserIcon());
                    oldFile.delete();
                }
            }
            userInfo.setNickName(response.getNickName());
            getUserIcon(userInfo, response.getIcon());
        });
        nfViewModel = new ViewModelProvider(this).get(NewFriendViewModel.class);
        nfViewModel.contactGet();
        nfViewModel.getContactsData().observe(this, response -> {
            if (response == null) {
                return;
            }
            List<String> friendNames = response.getFriendNames();
            List<String> friendIcons = response.getFriendIcons();
            List<String> friendNickNames = response.getFriendNickNames();
            for (int i=0; i<friendNames.size(); i++) {
                FriendRecord friendRecord = LitePal.where("friendName = ?", friendNames.get(i)).findFirst(FriendRecord.class);
                if (friendRecord != null) continue;
                friendRecord = new FriendRecord(friendNames.get(i), friendNickNames.get(i));
                getIconAndSave(friendRecord, friendIcons.get(i));
            }
            UserInfo userInfo = LitePal.where("username = ?", username).findFirst(UserInfo.class);
            if (userInfo == null) userInfo = new UserInfo(username);
            userInfo.setFriendNames(friendNames);
            userInfo.save();
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
                intent.putExtra("type", "create");
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
                    // ????????????
                    break;
            }
        }
    };

    public void getIconAndSave(final FriendRecord friendRecord, final String friendIcon) {
        //??????????????????????????????
        new Thread(() -> {
            String path = "http://8.140.133.34:7262/" + friendIcon;
            Bitmap bitmap = null;
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
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } else {
                    Log.d("HttpError", "??????????????????");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) return;
            File file = SaveBitmap2Png(bitmap);
            friendRecord.setIconPath(file.getAbsolutePath());
            friendRecord.save();
        }).start();
    }

    public void getUserIcon(final UserInfo userInfo, final String iconPath) {
        new Thread(() -> {
            String path = "http://8.140.133.34:7262/" + iconPath;
            Bitmap bitmap = null;
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
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                } else {
                    Log.d("HttpError", "??????????????????");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap == null) return;
            File file = SaveBitmap2Png(bitmap);
            if (file == null) return;
            userInfo.setUserIcon(file.getAbsolutePath());
            userInfo.save();
        }).start();
    }

    public File SaveBitmap2Png(Bitmap bitmap) {
        String storagePath = this.getExternalCacheDir().getAbsolutePath()
                + File.separator + "MyWeChat" + File.separator + "pictures";
        String uuid = UUID.randomUUID().toString();
        File file = new File(storagePath);
        file.mkdirs();
        file = new File(storagePath + File.separator
                + uuid + ".png");
        try {
            FileOutputStream fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}