package com.example.mywechat.ui.contacts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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

import com.example.mywechat.Activities.Chat.chatFragment.ChatBubble;
import com.example.mywechat.Activities.NewFriend.FriendApplyActivity;
import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.viewmodel.NewFriendViewModel;
import com.example.mywechat.Util.FileUtil;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ContactFragment extends Fragment {

    private Button friendApplyButton;
    private RecyclerView recyclerView;
    private NewFriendViewModel NfViewModel;
    private String username;

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

        // 设置LayoutManager及Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = view.findViewById(R.id.contacts_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        NfViewModel.contactGet();
        NfViewModel.getContactsData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                return;
            }
            List<String> friendNames = response.component1();
            List<String> friendIcons = response.component3();
            getIcons(friendNames, friendIcons);
        });

        //App app = (App) getActivity().getApplication();
        //List<FriendRecord> tmpList = LitePal.where("userName = ?", app.getUsername()).find(FriendRecord.class);
        //FriendRecord friendRecord = null;

//        if (friendRecord != null) {
//            LinkedList<Contact> contacts = new LinkedList<>();
//            List<String> friendsName = friendRecord.getFriendsName();
//            List<String> friendsIcon = friendRecord.getFriendsIcon();
//            for (int i = 0; i < friendsName.size(); i++) {
//                try {
//                    FileInputStream fis = new FileInputStream(friendsIcon.get(i));
//                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
//                    contacts.add(new Contact(friendsName.get(i), bitmap));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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

    private Handler handler = new Handler(Looper.myLooper()) {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                LinkedList<Contact> contacts = (LinkedList<Contact>) msg.obj;
                recyclerView.setAdapter(new ContactAdapter(contacts));
                for (Contact contact : contacts) {
                    FriendRecord friendRecord = new FriendRecord(contact.getNickname(), FileUtil.bitmap2byte(contact.getAvatarIcon()));
                    friendRecord.save();
                }
            }
        }
    };

    public void getIcons(final List<String> friendNames, final List<String> usericon) {
        //开启一个线程用于联网
        new Thread(() -> {
            List<Bitmap> bitmaps = new ArrayList<>();
            LinkedList<Contact> contacts = new LinkedList<>();
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
            for (int i=0; i<friendNames.size(); i++) {
                contacts.add(new Contact(friendNames.get(i), bitmaps.get(i)));
            }
            Message msg = new Message();
            msg.what = 0;
            msg.arg1 = arg1;
            msg.obj = contacts;
            handler.sendMessage(msg);
        }).start();
    }

    /** TODO 保存bitmap到外部存储的方法法
    public static File saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}