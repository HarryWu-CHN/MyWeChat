package com.example.mywechat.ui.discover;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.ui.comment.CommentAdapter;
import com.example.mywechat.viewmodel.DiscoverViewModel;

import java.util.ArrayList;
import java.util.LinkedList;

import dagger.hilt.android.AndroidEntryPoint;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.DiscoverViewHolder> {
    private View mParent;
    private LinkedList<Discover> data;
    private String mName;
    private String commentId;
    private DiscoverViewModel discoverViewModel;

    public DiscoverAdapter(LinkedList<Discover> data, DiscoverViewModel discoverViewModel, String mName) {
        this.data = data;
        this.discoverViewModel = discoverViewModel;
        this.mName = mName;
    }

    @Override
    public int getItemViewType(int position) {
        return this.data.get(position).getImageCount();
    }

    @NonNull
    @Override
    public DiscoverAdapter.DiscoverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mParent = (View) parent.getParent();

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
        ArrayList<Bitmap> imagesId = discover.getImages();
        for (int i = 0; i < holder.imageCount; i++) {
            images[i].setImageBitmap(imagesId.get(i));
        }

        // 点赞按钮
        holder.getLikeButton().setOnClickListener(v -> {
            String[] likeUsers = ((String) holder.getThumbUsers().getText()).split(", ");
            boolean mLiked = false;
            for (String likeUser : likeUsers) {
                if (likeUser.equals(mName)) {
                    mLiked = true;
                    break;
                }
            }

            if (mLiked) {
                holder.getLikeButton().setImageResource(R.drawable.icon_like_blue);
                discoverViewModel.thumb(this.data.get(position).getDiscoverId(), "0");
            } else {
                holder.getLikeButton().setImageResource(R.drawable.icon_lick_red);
                discoverViewModel.thumb(this.data.get(position).getDiscoverId(), "1");
            }
            discoverViewModel.discover(0);
        });

        // 评论按钮
        holder.getCommentButton().setOnClickListener(v -> {
            this.commentId = this.data.get(position).getDiscoverId();
            InputMethodManager inputMethodManager =
                    (InputMethodManager) this.mParent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            this.mParent.findViewById(R.id.commentLayout).setVisibility(View.VISIBLE);
            // editText.setInputType(EditorInfo.TYPE_CLASS_TEXT);
            EditText editText = this.mParent.findViewById(R.id.editCommentText);
            editText.requestFocus();

            View mParentParent = (View) this.mParent.getParent().getParent();
            mParentParent.findViewById(R.id.nav_view).setVisibility(View.GONE);
            mParentParent.findViewById(R.id.newDiscoverButton).setVisibility(View.GONE);
        });

        // 发送评论
        Button sendCommentButton = this.mParent.findViewById(R.id.sendCommentButton);
        sendCommentButton.setOnClickListener(v -> {
            EditText editText = this.mParent.findViewById(R.id.editCommentText);
            String msg = editText.getText().toString();

            discoverViewModel.comment(commentId, null, msg);

            InputMethodManager inputMethodManager =
                    (InputMethodManager) this.mParent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.mParent.findViewById(R.id.editCommentText).getWindowToken(), 0);

            View mParentParent = (View) this.mParent.getParent().getParent();
            mParentParent.findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
            mParentParent.findViewById(R.id.newDiscoverButton).setVisibility(View.VISIBLE);
            this.mParent.findViewById(R.id.commentLayout).setVisibility(View.GONE);

            discoverViewModel.discover(0);
        });

        // 点击外部隐藏评论输入框
        holder.getItemView().setOnClickListener(v -> {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) this.mParent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.mParent.findViewById(R.id.editCommentText).getWindowToken(), 0);

            View mParentParent = (View) this.mParent.getParent().getParent();
            mParentParent.findViewById(R.id.nav_view).setVisibility(View.VISIBLE);
            mParentParent.findViewById(R.id.newDiscoverButton).setVisibility(View.VISIBLE);
            this.mParent.findViewById(R.id.commentLayout).setVisibility(View.GONE);
        });

        boolean hideThumb = false;
        boolean hideComments = false;

        // 处理点赞
        if (this.data.get(position).getThumbUsers() != null && this.data.get(position).getThumbUsers().size() > 0) {
            StringBuilder likeStr = new StringBuilder();
            for (String likeUser : this.data.get(position).getThumbUsers()) {
                likeStr.append(likeUser);
                likeStr.append(", ");
                if (likeUser.equals(mName)) {
                    holder.getLikeButton().setImageResource(R.drawable.icon_lick_red);
                }
            }
            likeStr.delete(likeStr.length() - 2, likeStr.length());
            holder.getThumbUsers().setText(likeStr.toString());
        } else {
            hideThumb = true;
            holder.getIconLike().setVisibility(View.INVISIBLE);
            holder.getThumbUsers().setVisibility(View.INVISIBLE);
            holder.getLikes_comments_layout().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }

        // 为评论添加Adapter
        if (this.data.get(position).getComments() != null && this.data.get(position).getComments().size() > 0) {
            holder.getCommentsView().setAdapter(new CommentAdapter(this.data.get(position).getComments()));
        }
        else {
            hideComments = true;
            holder.getLikes_comments_layout().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }

        if (hideComments && ! hideThumb) {
            holder.getCommentsView().setVisibility(View.GONE);
        } else if (hideThumb && !hideComments) {
            holder.getItemView().findViewById(R.id.likeLayout).setVisibility(View.GONE);
        } else if (hideComments && hideThumb) {
            holder.getLikes_comments_layout().setVisibility(View.INVISIBLE);
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
        private final ImageButton likeButton;
        private final ImageButton commentButton;
        private final LinearLayout likes_comments_layout;
        private final ImageView iconLike;
        private final TextView thumbUsers;
        private final RecyclerView commentsView;

        private final View itemView;

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

            this.likeButton = itemView.findViewById(R.id.likeButton);
            this.commentButton = itemView.findViewById(R.id.commentButton);

            this.likes_comments_layout = itemView.findViewById(R.id.likes_and_comments_layout);

            this.iconLike = itemView.findViewById(R.id.icon_like);
            this.thumbUsers = itemView.findViewById(R.id.likeUserText);

            this.commentsView = itemView.findViewById(R.id.commentsView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
            this.commentsView.setLayoutManager(linearLayoutManager);

            this.itemView = itemView;
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

        public ImageButton getLikeButton() {
            return likeButton;
        }

        public ImageButton getCommentButton() {
            return commentButton;
        }

        public View getItemView() {
            return this.itemView;
        }

        public LinearLayout getLikes_comments_layout() {
            return this.likes_comments_layout;
        }

        public ImageView getIconLike() {
            return iconLike;
        }

        public TextView getThumbUsers() {
            return thumbUsers;
        }

        public RecyclerView getCommentsView() {
            return commentsView;
        }
    }


}

