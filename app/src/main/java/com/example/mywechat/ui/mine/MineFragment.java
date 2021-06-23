package com.example.mywechat.ui.mine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.Util.FileUtil;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.viewmodel.InfoViewModel;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class MineFragment extends Fragment {
    private ImageView myAvatar;
    private TextView myNickName;
    private TextView myUserName;
    private Button settingButton;

    private String userName = null;
    private String nickName = null;
    private Bitmap avatar = null;

    private InfoViewModel infoViewModel;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExploreFragment.
     */
    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        hideActionBar(view);
        infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        myAvatar = view.findViewById(R.id.mine_avatar);
        myNickName = view.findViewById(R.id.myNickName);
        myUserName = view.findViewById(R.id.myUserName);
        settingButton = view.findViewById(R.id.setting_button);

        userName = ((App) getActivity().getApplication()).getUsername();
        myUserName.setText(userName);
        UserInfo userInfo = null;
        if (userName != null)
            userInfo = LitePal.where("username = ?", userName).findFirst(UserInfo.class);

        infoViewModel.getUserInfoLiveData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                return;
            }

            nickName = response.getNickName();
            myNickName.setText(nickName);
            getIcon(response.getIcon());
        });

        if (userInfo == null || userInfo.getUserIcon() == null) {
            infoViewModel.userGet(userName);
        } else {
            nickName = userInfo.getNickName();
            avatar = BitmapFactory.decodeFile(userInfo.getUserIcon());
            myNickName.setText(nickName);
            myAvatar.setImageBitmap(avatar);
        }

        settingButton.setOnClickListener(v -> {
            if (avatar == null) {
                return;
            }

            ByteArrayOutputStream iconBytes = new ByteArrayOutputStream();
            avatar.compress(Bitmap.CompressFormat.JPEG, 100, iconBytes);
            Intent intent = new Intent(getActivity(), InfoActivity.class);
            intent.putExtra("userName", userName);
            intent.putExtra("nickName", nickName);
            intent.putExtra("avatarBytes", iconBytes.toByteArray());
            startActivity(intent);
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("mine");
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    private void hideActionBar(@NonNull View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        ((AppCompatActivity) context).getSupportActionBar().hide();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    avatar = bitmap;
                    myAvatar.setImageBitmap(bitmap);
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

    public void getIcon(final String avatarIcon) {
        //开启一个线程用于联网
        new Thread(() -> {
            int arg1 = 0;
            String path = "http://8.140.133.34:7262/" + avatarIcon;
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
            msg.obj = bitmap;
            handler.sendMessage(msg);
        }).start();
    }
}