package com.example.mywechat.ui.pickAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.mywechat.R;

public class ImagePick {
    private final Bitmap image;

    public ImagePick(Context context) {
        this.image = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_plus_rect);
    }

    public ImagePick(Bitmap bitmap) {
        this.image = bitmap;
    }

    public Bitmap getImage() {
        return image;
    }
}
