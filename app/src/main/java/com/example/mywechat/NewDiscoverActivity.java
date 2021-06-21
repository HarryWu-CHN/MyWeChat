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
    private VideoView uploadVideo;
    private RecyclerView imageUploadRecyclerView;
    private int maxImageCnt = 4;
    private EditText contentText;
    private boolean selectVideo = false;
    private List<ImagePick> selectImages;
    private List<File> selectFiles;
    private ImagePickAdapter adapter;
    private ImageView previewImage;
    private VideoView previewVideo;
    private Dialog previewDialog;
    private DiscoverViewModel discoverViewModel;

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_new_discover);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        contentText = findViewById(R.id.newDiscoverContent);

        /* 图片预览Dialog */
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

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // TODO: 草稿功能，暂时不实现
            finish();
        });

        postDiscoverButton = findViewById(R.id.postDiscoverButton);
        postDiscoverButton.setOnClickListener(v -> {
            String msgType;
            if (this.selectFiles.size() == 0) {
                msgType = "0";
                discoverViewModel.discoverPost(msgType, contentText.getText().toString(), null);
            } else if (!selectVideo) {
                msgType = "1";
                discoverViewModel.discoverPost(msgType, contentText.getText().toString(), selectFiles);
            } else {
                msgType = "2";
                discoverViewModel.discoverPost(msgType, contentText.getText().toString(), selectFiles);
            }
            finish();
        });

        uploadVideo = findViewById(R.id.discoverVideo);
        previewVideo = new VideoView(this);
//        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//        LayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        previewVideo.setLayoutParams(LayoutParams);
        uploadVideo.setOnClickListener(v -> {
            previewDialog.setContentView(previewVideo);
            previewVideo.setVisibility(View.VISIBLE);
            previewDialog.show();
            previewVideo.start();
        });
        previewVideo.setOnClickListener(v -> {
            previewDialog.dismiss();
        });

        imageUploadRecyclerView = findViewById(R.id.imageUploadRecyclerView);
        //imageUploadRecyclerView.setAdapter(adapter);
        initUploadView();
    }

    private void initUploadView() {
        selectFiles = new LinkedList<>();
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        selectImages = new ArrayList<>();
        adapter = new ImagePickAdapter(this, selectImages, maxImageCnt);
        adapter.setOnItemClickListener(this);
        imageUploadRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }

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
        if (requestCode == REQUEST_CODE_SELECT) {
            if (data != null && resultCode == RESULT_OK) {
                //Bitmap bitmap = getStorageImageBitMap(data);
                //selectImages.add(new ImagePick(bitmap));
                //adapter.setImages(selectImages);

                Uri uri = data.getData();
                FileUtil file = new FileUtil();
                String filePath = file.getFilePath(this, uri);
                switch (file.selectType) {
                    case FileUtil.IMAGE:
                        selectFiles.add(new File(filePath));
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        selectImages.add(new ImagePick(bitmap));
                        adapter.setImages(selectImages);
                        break;
                    case FileUtil.VIDEO:
                        selectVideo = true;
                        selectFiles = new LinkedList<>();
                        selectFiles.add(new File(filePath));
                        imageUploadRecyclerView.setVisibility(View.GONE);
                        uploadVideo.setVideoPath(filePath);
                        uploadVideo.seekTo(100);
                        previewVideo.setVideoPath(filePath);
                        uploadVideo.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    private Bitmap getStorageImageBitMap(Intent data) {
        Uri uri = data.getData();
        String imagePath = getFilePath(this, uri);
        selectFiles.add(new File(imagePath));
        Log.d("imagePath:", imagePath);

        return BitmapFactory.decodeFile(imagePath);
    }

    private String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() +"/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {split[1]};
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };

            try {
                Cursor cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String imagePath = cursor.getString(column_index);
                    cursor.close();
                    return imagePath;
                }
            } catch (Exception ignored) {
                // ignore
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return"com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return"com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return"com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private boolean isGooglePhotosUri(Uri uri) {
        return"com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
