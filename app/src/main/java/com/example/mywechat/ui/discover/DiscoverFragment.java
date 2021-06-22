package com.example.mywechat.ui.discover;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

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
    private DiscoverAdapter adapter;
    private RecyclerView recyclerView;
    private DiscoverViewModel discoverViewModel;

    private ImageView previewImage;
    private VideoView previewVideo;
    private Dialog previewDialog;

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

        initPreviewDialog();
        BindViewModel();

        recyclerView = view.findViewById(R.id.discover_recyclerview);
        // 设置LayoutManager及Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        initAdapterAndListener();
        recyclerView.setAdapter(adapter);

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
            startActivityForResult(intent, 0);
            //startActivity(intent);
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

    private void BindViewModel() {
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        discoverViewModel.discover(0);

        discoverViewModel.getLiveData().observe(getViewLifecycleOwner(), response -> {
            discoverViewModel.discover(0);
        });

        discoverViewModel.getDiscoverData().observe(getViewLifecycleOwner(), response -> {
            if (response == null) {
                return;
            }

            List<DiscoverInfo> discoverList = response.component2();
            LinkedList<Discover> data = new LinkedList<>();
            ArrayList<Pair<String, ArrayList<String>>> download = new ArrayList<>();
            for (DiscoverInfo info : discoverList) {
                Discover discover = new Discover();
                discover.setAvatarIcon(R.drawable.avatar1);
                discover.setDiscoverId(info.component1());
                discover.setNickname(info.component2());
                discover.setText(info.component3());
                discover.setDiscoverType(info.component4());
                discover.setPublishedTime(info.component6());

                ArrayList<String> thumbUsers = null;
                ArrayList<Comment> comments = null;
                if (info.component7() != null) {
                    thumbUsers = new ArrayList<>(info.component7());
                }
                if (info.component8() != null) {
                    comments = new ArrayList<>();
                    for (DiscoverComment comment : info.component8()) {
                        comments.add(new Comment(comment.component1(), comment.component2(), comment.component3()));
                    }
                }
                discover.setThumbUsers(thumbUsers);
                discover.setComments(comments);

                switch (info.component4()) {
                    case "PHOTO":
                        ArrayList<String> images = new ArrayList<>(info.component5());
                        download.add(new Pair<>(info.component1(), images));
                        break;
                    case "VIDEO":
                        discover.setVideoUrl(info.component5().get(0));
                        break;
                }
                data.add(discover);
            }
            adapter.setDiscoverData(data);
            for (Pair<String, ArrayList<String>> downloadPair : download) {
                getImages(downloadPair.first, downloadPair.second);
            }
        });
    }

    private void initAdapterAndListener() {
        adapter = new DiscoverAdapter(discoverViewModel, ((App) getActivity().getApplication()).getUsername());
        adapter.setOnItemClickListener(new DiscoverAdapter.onRecyclerItemClickerListener() {
            @Override
            public void onRecyclerItemClick(View view, String discoverType, Object data) {
                switch (discoverType) {
                    case "PHOTO":
                        Bitmap bitmap = (Bitmap) data;
                        previewImage.setImageBitmap(bitmap);
                        previewDialog.setContentView(previewImage);
                        previewDialog.show();
                        break;
                    case "VIDEO":
                        Uri videoUri = (Uri) data;
                        previewVideo.setVideoURI(videoUri);
                        previewDialog.setContentView(previewVideo);
                        previewDialog.show();
                        previewVideo.start();
                        break;
                    default:
                        // ignored
                        break;
                }
            }
        });
    }

    private void initPreviewDialog() {
        // 预览Dialog
        previewDialog = new Dialog(getActivity(), R.style.FullActivity);
        WindowManager.LayoutParams attributes = previewDialog.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        //attributes.gravity = Gravity.CENTER_VERTICAL;
        previewDialog.getWindow().setAttributes(attributes);

        previewImage = new ImageView(getActivity());
        previewImage.setOnClickListener(v -> {
            previewDialog.dismiss();
        });

        previewVideo = new VideoView(getActivity());
//        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//        LayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        previewVideo.setLayoutParams(LayoutParams);
        previewVideo.setOnClickListener(v -> {
            previewDialog.dismiss();
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Pair<String, ArrayList<Bitmap>> bitmaps = (Pair<String, ArrayList<Bitmap>>) msg.obj;
                    adapter.updateDiscoverData(bitmaps.first, bitmaps.second);
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

    private void getImages(String discoverId, ArrayList<String> imagePaths) {
        new Thread(() -> {
            int arg1 = 0;
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
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
            msg.obj = new Pair<>(discoverId, bitmaps);
            handler.sendMessage(msg);
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        discoverViewModel.discover(0);
    }
}