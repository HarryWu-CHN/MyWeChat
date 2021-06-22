package com.example.mywechat;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.ui.pickAdapter.ImagePick;
import com.example.mywechat.ui.pickAdapter.ImagePickAdapter;
import com.example.mywechat.viewmodel.DiscoverViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NewDiscoverActivity extends AppCompatActivity implements ImagePickAdapter.OnRecyclerViewItemClickListener {
    private ImageButton backButton;
    private Button postDiscoverButton;

    private EditText contentText;

    private final int spanCount = 2;
    private final int maxImageCnt = 4;
    private ImagePickAdapter adapter;
    private RecyclerView imageUploadRecyclerView;
    private List<ImagePick> selectImages;

    private boolean selectVideo = false;
    private VideoView uploadVideo;
    private Button selectVideoButton;

    private ImageView previewImage;
    private VideoView previewVideo;
    private Dialog previewDialog;

    private List<File> selectFiles;
    private DiscoverViewModel discoverViewModel;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_IMAGE = 100;
    public static final int REQUEST_CODE_VIDEO = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_discover);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        contentText = findViewById(R.id.newDiscoverContent);

        // 返回按钮
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });

        // 发送朋友圈按钮
        postDiscoverButton = findViewById(R.id.postDiscoverButton);
        postDiscoverButton.setOnClickListener(v -> {
            String msgType;
            if (this.selectFiles.size() == 0) {
                msgType = "0";
                discoverViewModel.discoverPost(msgType, contentText.getText().toString(), null);
            } else if (!selectVideo) {
                msgType = "1";
                if (selectFiles.size() > 1) {
                    discoverViewModel.discoverMulPost(msgType, contentText.getText().toString(), selectFiles);
                } else {
                    discoverViewModel.discoverPost(msgType, contentText.getText().toString(), selectFiles.get(0));
                }
            } else {
                msgType = "2";
                discoverViewModel.discoverPost(msgType, contentText.getText().toString(), selectFiles.get(0));
            }
            finish();
        });

        selectFiles = new LinkedList<>();
        selectImages = new ArrayList<>();

        initImageSelect();
        initVideoSelect();
        initPreviewDialog();
    }

    private void initImageSelect() {
        imageUploadRecyclerView = findViewById(R.id.imageUploadRecyclerView);
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        adapter = new ImagePickAdapter(this, selectImages, maxImageCnt);
        adapter.setOnItemClickListener(this);
        imageUploadRecyclerView.setAdapter(adapter);
    }

    private void initVideoSelect() {
        uploadVideo = findViewById(R.id.discoverVideo);
        selectVideoButton = findViewById(R.id.selectVideoButton);

        selectVideoButton.setOnClickListener(v -> {
            checkAuthority();
            openVideo();
        });
    }

    private void initPreviewDialog() {
        // 预览Dialog
        previewDialog = new Dialog(this, R.style.FullActivity);
        WindowManager.LayoutParams attributes = previewDialog.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        //attributes.gravity = Gravity.CENTER_VERTICAL;
        previewDialog.getWindow().setAttributes(attributes);

        previewImage = new ImageView(this);
        previewImage.setOnClickListener(v -> {
            previewDialog.dismiss();
        });

        uploadVideo.setOnClickListener(v -> {
            previewDialog.setContentView(previewVideo);
            previewVideo.setVisibility(View.VISIBLE);
            previewDialog.show();
            previewVideo.start();
        });

        previewVideo = new VideoView(this);
//        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//        LayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        previewVideo.setLayoutParams(LayoutParams);
        previewVideo.setOnClickListener(v -> {
            previewDialog.dismiss();
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                checkAuthority();
                openAlbum();
                break;
            default:
                Bitmap previewBitmap = selectImages.get(position).getImage();
                previewImage.setImageBitmap(previewBitmap);
                previewDialog.setContentView(previewImage);
                previewDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filePath = FileUtil.getFilePath(this, uri);
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    selectFiles.add(new File(filePath));
                    selectImages.add(new ImagePick(bitmap));
                    adapter.setImages(selectImages);
                    break;
                case REQUEST_CODE_VIDEO:
                    selectVideo = true;
                    selectFiles = new LinkedList<>();
                    selectFiles.add(new File(filePath));
                    uploadVideo.setVideoPath(filePath);
                    uploadVideo.seekTo(100);
                    previewVideo.setVideoPath(filePath);
                    uploadVideo.setVisibility(View.VISIBLE);
                    imageUploadRecyclerView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void checkAuthority() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }

    private void openVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, REQUEST_CODE_VIDEO);
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }
}
