package com.example.mywechat.ui.discover.comment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> data;

    public CommentAdapter(ArrayList<Comment> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_comment, parent, false);

        return new CommentAdapter.CommentViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = this.data.get(position);

        String NickName = comment.getNickName() + ": ";
        holder.getNickName().setText(NickName);
        holder.getText().setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        private final TextView nickName;
        private final TextView text;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nickName = itemView.findViewById(R.id.commentNickName);
            this.text = itemView.findViewById(R.id.commentText);
        }

        public TextView getNickName() {
            return nickName;
        }

        public TextView getText() {
            return text;
        }
    }
}
