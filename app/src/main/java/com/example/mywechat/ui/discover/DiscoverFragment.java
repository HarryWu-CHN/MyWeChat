package com.example.mywechat.ui.discover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.App;
import com.example.mywechat.NewDiscoverActivity;
import com.example.mywechat.R;
import com.example.mywechat.api.DiscoverComment;
import com.example.mywechat.api.DiscoverInfo;
import com.example.mywechat.ui.comment.Comment;
import com.example.mywechat.viewmodel.DiscoverViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DiscoverFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private DiscoverViewModel discoverViewModel;
    private List<DiscoverInfo> discoverList;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExploreFragment.
     */
    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        hideActionBar(view);
        recyclerView = view.findViewById(R.id.discover_recyclerview);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        discoverViewModel.discover(0);

        discoverViewModel.getLiveData().observe(getViewLifecycleOwner(), response -> {
            discoverViewModel.discover(0);
        });

        discoverViewModel.getDiscoverData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                return;
            }

            discoverList = response.component2();
            getImages();
        });

        // 设置LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        // 设置朋友圈分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // commentEditText监听
        EditText commentEditText = view.findViewById(R.id.editCommentText);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                view.findViewById(R.id.sendCommentButton).setEnabled(!TextUtils.isEmpty(commentEditText.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 新朋友圈
        FloatingActionButton newDiscoverButton = view.findViewById(R.id.newDiscoverButton);
        newDiscoverButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewDiscoverActivity.class);
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
        return inflater.inflate(R.layout.fragment_discover, container, false);
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
                    LinkedList<Discover> discovers = new LinkedList<>();
                    List<Pair<String, Bitmap>> bitmaps = (List<Pair<String, Bitmap>>) msg.obj;
                    for (DiscoverInfo discover : discoverList) {
                        String discoverId = discover.component1();
                        String nickName = discover.component2();
                        String text = discover.component3();
                        String time = discover.component6();
                        ArrayList<Bitmap> images = new ArrayList<>();
                        for (Pair<String, Bitmap> bitmap : bitmaps) {
                            if (discover.component1().equals(bitmap.first)) {
                                images.add(bitmap.second);
                            }
                        }

                        ArrayList<String> thumbUsers = null;
                        ArrayList<Comment> comments = null;
                        if (discover.component7() != null) {
                            thumbUsers = new ArrayList<>(discover.component7());
                        }
                        if (discover.component8() != null) {
                            comments = new ArrayList<>();
                            for (DiscoverComment comment : discover.component8()) {
                                comments.add(new Comment(comment.component1(), comment.component2(), comment.component3()));
                            }
                        }

                        discovers.add(new Discover(discoverId, nickName, R.drawable.avatar1, text,
                                time, images, thumbUsers, comments));
                    }

                    recyclerView.setAdapter(new DiscoverAdapter(discovers, discoverViewModel,
                            ((App) getActivity().getApplication()).getUsername()));
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

    private void getImages() {
        new Thread(() -> {
            List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
            int arg1 = 0;
            for (DiscoverInfo discover : discoverList) {
                if (discover.component4().equals("ONLY_TEXT")) {
                    continue;
                }
                List<String> imagePaths = discover.component5();
                for (String path : imagePaths) {
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
                            bitmaps.add(new Pair<>(discover.component1(), bitmap));
                            inputStream.close();
                        } else {
                            arg1 = 1;
                            bitmaps.add(new Pair<>(discover.component1(), null));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        bitmaps.add(null);
                        arg1 = 2;
                    }
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