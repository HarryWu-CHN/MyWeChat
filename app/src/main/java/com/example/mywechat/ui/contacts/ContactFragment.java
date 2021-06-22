package com.example.mywechat.ui.contacts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.Activities.NewFriend.FriendApplyActivity;
import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.ui.Group.GroupActivity;
import com.example.mywechat.viewmodel.NewFriendViewModel;
import com.example.mywechat.Util.FileUtil;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactFragment extends Fragment {
    private String username;

    private Button friendApplyButton;
    private Button myGroupButton;

    private LinkedList<Contact> contacts;
    private ContactAdapter adapter;
    private RecyclerView recyclerView;

    private NewFriendViewModel NfViewModel;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showActionBar(view);
        username = ((App) requireActivity().getApplication()).getUsername();
        NfViewModel = new ViewModelProvider(this)
                .get(NewFriendViewModel.class);

        friendApplyButton = view.findViewById(R.id.friendApplyButton);
        friendApplyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FriendApplyActivity.class);
            startActivity(intent);
        });

        // 测试
        myGroupButton = view.findViewById(R.id.myGroupButton);
        myGroupButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GroupActivity.class);
            startActivity(intent);
        });

        // 设置LayoutManager及Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.contacts_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        NfViewModel.contactGet();
        NfViewModel.getContactsData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                return;
            }

            contacts = new LinkedList<>();
            List<String> friendNames = response.getFriendNames();
            List<String> friendNickNames = response.getFriendNickNames();
            List<String> friendIcons = response.getFriendIcons();
            for (int i = 0; i < friendNames.size(); i++) {
                String friendName = friendNames.get(i);
                String friendNickName = friendNickNames.get(i);
                String friendIcon = friendIcons.get(i);
                FriendRecord friendRecord = LitePal.where("friendName = ?", friendName).findFirst(FriendRecord.class);
                if (friendRecord == null || friendRecord.getIconPath() == null) {
                    contacts.add(new Contact(friendName, friendNickName));
                    friendRecord = new FriendRecord(friendName, friendNickName);
                    getIconAndSave(friendRecord, friendIcon, i);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(friendRecord.getIconPath());
                    contacts.add(new Contact(friendName, friendNickName, bitmap));
                }
            }
            adapter.setContacts(contacts);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    private void showActionBar(@NonNull View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        ((AppCompatActivity) context).getSupportActionBar().show();
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
                    adapter.setContacts(contacts);
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