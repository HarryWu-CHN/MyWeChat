package com.example.mywechat.ui.mine;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.mywechat.Activities.Chat.ChatActivity;
import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.api.UserGetResponse;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.viewmodel.InfoViewModel;
import com.example.mywechat.viewmodel.InfoViewModel;
import com.example.mywechat.R;
import com.franmontiel.persistentcookiejar.persistence.SerializableCookie;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.Cookie;

@AndroidEntryPoint
public class InfoActivity extends AppCompatActivity {
    private ImageButton backButton;
    private Button avatarButton;
    private Button nickNameButton;
    private Button passwordButton;
    private ImageView myAvatar;
    private ImageView testImageView;
    private TextView myNickName;
    private TextView myUserName;
    private InfoViewModel infoViewModel;
    SharedPreferences sharedPreferences;

    private String userName;
    private String nickName;
    private Bitmap avatar;

    private Toast resultToast;

    private static final int PHOTO = 1;
    private static final int ALBUM = 2;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        nickName = intent.getStringExtra("nickName");
        byte[] avatarBytes = intent.getByteArrayExtra("avatarBytes");
        avatar = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);

        resultToast = Toast.makeText(this, "成功修改密码", Toast.LENGTH_SHORT);
        setInfoView();
        infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
    }

    private void setInfoView() {
        setContentView(R.layout.activity_user_info);

        backButton = findViewById(R.id.backButton);
        avatarButton = findViewById(R.id.avatarButton);
        nickNameButton = findViewById(R.id.nickNameButton);
        passwordButton = findViewById(R.id.passwordButton);
        myAvatar = findViewById(R.id.avatarThumbnail);
        testImageView = findViewById(R.id.testImageView);
        myNickName = findViewById(R.id.editMyNickName);
        myUserName = findViewById(R.id.myUserName);

        myUserName.setText(userName);
        myNickName.setText(nickName);
        myAvatar.setImageBitmap(avatar);

        initInfoButtons();
    }

    private void initInfoButtons() {
        backButton.setOnClickListener(v -> {
            List<Cookie> cookies = new ArrayList<>(sharedPreferences.getAll().size());
            for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
                String serializedCookie = (String) entry.getValue();
                Cookie cookie = new SerializableCookie().decode(serializedCookie);
                if (cookie != null) {
                    cookies.add(cookie);
                }
            }
            Log.d("Cookies" , cookies.toString());
            finish();
        });

        avatarButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }

            openAlbum();
        });

        nickNameButton.setOnClickListener(v -> {
            setNickNameView();
        });

        passwordButton.setOnClickListener(v -> {
            setPasswordView();
        });
    }
    private void setPasswordView() {
        setContentView(R.layout.fragment_edit_password);

        ImageButton backToInfoButton_p = findViewById(R.id.backToInfoButton_p);
        Button savePasswordButton = findViewById(R.id.savePasswordButton);
        EditText oldPasswordText = findViewById(R.id.oldPasswordText);
        EditText newPasswordText = findViewById(R.id.newPasswordText);

        backToInfoButton_p.setOnClickListener(v -> {
            setInfoView();
        });

        savePasswordButton.setOnClickListener(v -> {
            infoViewModel.callPasswordEdit(oldPasswordText.getText().toString(), newPasswordText.getText().toString());
            infoViewModel.getPasswordEditResult().observe(this, response -> {
                if (response != null)
                {
                    if (!response) {
                        Log.d("修改密码失败",response.toString()+"a");
                        resultToast.setText("修改密码失败");
                        resultToast.show();
                    } else if (response) {
                        Log.d("修改密码成功",response.toString());
                        infoViewModel.getPasswordEditResult().setValue(null);
                        setInfoView();
                        resultToast.setText("成功修改密码");
                        resultToast.show();
                    }
                }

            });
        });


    }

    private void setNickNameView() {
        setContentView(R.layout.fragment_edit_nickname);

        ImageButton backToInfoButton = findViewById(R.id.backToInfoButton);
        Button saveNickNameButton = findViewById(R.id.saveNickNameButton);
        EditText newNickNameText = findViewById(R.id.newNickNameText);
        newNickNameText.setText(nickName);

        backToInfoButton.setOnClickListener(v -> {
            setInfoView();
        });

        saveNickNameButton.setOnClickListener(v -> {
            setInfoView();
            myNickName.setText(newNickNameText.getText());
            infoViewModel.callUserEdit(myNickName.getText().toString(), null);
            Toast toast=Toast.makeText(getApplicationContext(), "修改昵称成功", Toast.LENGTH_SHORT);
            toast.show();
        });
        
    }

    private void handleStorageImage(Intent data) {
        Uri uri = data.getData();
        String imagePath = getFilePath(this, uri);

        Log.d("imagePath:", imagePath);

        // 展示图片选择
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        myAvatar.setImageBitmap(bitmap);
        testImageView.setImageBitmap(bitmap);
        File file = new File(imagePath);
        infoViewModel.callUserEdit(null, file);
    }

    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PHOTO:
                // TODO: 拍照获取头像，暂不需要
                break;
            case ALBUM:
                if (resultCode == RESULT_OK) {
                    handleStorageImage(data);
                }
                break;
        }
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
