package com.example.mywechat;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.ui.pickAdapter.ImagePickAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewDiscoverActivity extends AppCompatActivity {
    private ImageButton backButton;
    private Button postDiscoverButton;
    private RecyclerView imageUploadRecyclerView;
    private int maxImageCnt = 9;
    private List<Object> selectImages;
    private ImagePickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discover);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // TODO: 草稿功能
            finish();
        });

        postDiscoverButton = findViewById(R.id.postDiscoverButton);
        postDiscoverButton.setOnClickListener(v -> {
            // TODO: 发送朋友圈
        });

        imageUploadRecyclerView = findViewById(R.id.imageUploadRecyclerView);
        initUploadView();

    }

    private void initUploadView() {
        imageUploadRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        selectImages = new ArrayList<>();
        adapter = new ImagePickAdapter(this, selectImages, maxImageCnt);
        adapter.setOnItemClickListener((ImagePickAdapter.OnRecyclerViewItemClickListener) this);
        adapter.setOnItemRemoveClick(() -> {
            adapter.setImages(adapter.getImages());
            adapter.notifyDataSetChanged();
            selectImages.clear();
            selectImages.addAll(adapter.getImages());
        });
        imageUploadRecyclerView.setAdapter(adapter);
    }
}
