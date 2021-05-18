package com.example.mywechat.ui.discover;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.ArrayList;
import java.util.LinkedList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {
    private LinkedList<Discover> data;

    public DiscoverAdapter(LinkedList<Discover> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return this.data.get(position).getImageCount();
    }

    @NonNull
    @Override
    public DiscoverAdapter.DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView;
        switch (viewType) {
            case 0:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_discover_0, parent, false);
                break;
            case 1:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_discover_1, parent, false);
                break;
            case 2:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_discover_2, parent, false);
                break;
            case 3:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_discover_3, parent, false);
                break;
            case 4:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_discover_4, parent, false);
                break;
            default:
                throw new IllegalArgumentException("viewType error!");
        }

        if (viewType == 0) {
            ImageButton likeButton = mView.findViewById(R.id.likeButton);
            likeButton.setOnClickListener(v -> {
                //TODO: 增加后端交互
                likeButton.setImageResource(R.drawable.icon_lick_red);
            });
        }
        return new DiscoverAdapter.DiscoverViewHolder(mView, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverViewHolder holder, int position) {
        Discover discover = this.data.get(position);

        holder.getAvatar().setImageResource(discover.getAvatarIcon());
        holder.getNickname().setText(discover.getNickname());
        holder.getPostText().setText(discover.getText());
        holder.getPublishedTime().setText(discover.getPublishedTime());

        ImageView[] images = holder.getImages();
        ArrayList<Integer> imagesId = discover.getImages();
        for (int i = 0; i < holder.imageCount; i++) {
            images[i].setImageResource(imagesId.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class DiscoverViewHolder extends RecyclerView.ViewHolder {
        public int imageCount;
        private final int[] imagesId = {R.id.image_1, R.id.image_2, R.id.image_3, R.id.image_4};
        private final ImageView discoverAvatarView;
        private final TextView discoverNickname;
        private final TextView discoverText;
        private final TextView discoverPublishedTime;
        private final ImageView[] discoverImages;

        public DiscoverViewHolder(@NonNull View itemView, int imageCount) {
            super(itemView);
            this.imageCount = imageCount;
            this.discoverAvatarView = itemView.findViewById(R.id.avatar_icon);
            this.discoverNickname = itemView.findViewById(R.id.nickname);
            this.discoverText = itemView.findViewById(R.id.text);
            this.discoverPublishedTime = itemView.findViewById(R.id.publishedTime);
            this.discoverImages = new ImageView[imageCount];
            for (int i = 0; i < imageCount; i++) {
                this.discoverImages[i] = itemView.findViewById(imagesId[i]);
            }
        }

        public ImageView getAvatar() {
            return this.discoverAvatarView;
        }

        public TextView getNickname() {
            return this.discoverNickname;
        }

        public TextView getPostText() {
            return this.discoverText;
        }

        public TextView getPublishedTime() {
            return this.discoverPublishedTime;
        }

        public ImageView[] getImages() {
            return this.discoverImages;
        }

    }


}

