package com.example.mywechat;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;

public class FileUtil {
    static public final int IMAGE = 1;
    static public final int VIDEO = 2;
    static public final int AUDIO = 3;
    public int selectType;

    public FileUtil() {
        // ignored
    }

    public String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                String filePath = Environment.getExternalStorageDirectory() +"/" + split[1];
                CheckFileType(filePath);
                return filePath;
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    this.selectType = IMAGE;
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    this.selectType = VIDEO;
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    this.selectType = AUDIO;
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[] {split[1]};
            }
        }

        if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                String filePath = uri.getLastPathSegment();
                CheckFileType(filePath);
                return filePath;
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
                    CheckFileType(imagePath);
                    return imagePath;
                }
            } catch (Exception ignored) {
                // ignore
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            String filePath = uri.getPath();
            CheckFileType(filePath);
            return filePath;
        }
        return null;
    }

    public String handleStorageImage(Context context, Intent data) {
        Uri uri = data.getData();
        String imagePath = getFilePath(context, uri);

        Log.d("imagePath:", imagePath);

        // 展示图片选择
        return imagePath;
    }

    private void CheckFileType(String filePath) {
        String typeStr = filePath.substring(filePath.length() - 3);
        switch (typeStr) {
            case "mp4":
                selectType = VIDEO;
                break;
            case "jpg":
            case "png":
                selectType = IMAGE;
                break;
            case "mp3":
                selectType = AUDIO;
                break;
        }
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
