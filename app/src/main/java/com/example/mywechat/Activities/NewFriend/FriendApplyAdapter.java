package com.example.mywechat.Activities.NewFriend;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.LinkedList;

public class FriendApplyAdapter extends RecyclerView.Adapter<FriendApplyAdapter.ApplyViewHolder>{
    private LinkedList<FriendApply> data;

    public FriendApplyAdapter(LinkedList<FriendApply> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public FriendApplyAdapter.ApplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_apply, parent, false);
        return new FriendApplyAdapter.ApplyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendApplyAdapter.ApplyViewHolder holder, int position) {
        FriendApply friendApply = data.get(position);
        holder.getApplyAvatar().setImageResource(friendApply.getFriendAvatar());
        holder.getApplyNickName().setText(friendApply.getFriendName());

        holder.getAgreeButton().setOnClickListener(v -> {
            // TODO: 同意申请
        });

        holder.getRefuseButton().setOnClickListener(v -> {
            // TODO: 拒绝申请
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ApplyViewHolder extends RecyclerView.ViewHolder {
        private ImageView applyAvatar;
        private TextView applyNickName;
        private ImageButton agreeButton;
        private ImageButton refuseButton;
        private View itemView;

        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.applyAvatar = itemView.findViewById(R.id.applyAvatar);
            this.applyNickName = itemView.findViewById(R.id.applyNickname);
            this.agreeButton = itemView.findViewById(R.id.agreeButton);
            this.refuseButton = itemView.findViewById(R.id.refuseButton);
            this.itemView = itemView;
        }

        public ImageView getApplyAvatar() {
            return applyAvatar;
        }

        public TextView getApplyNickName() {
            return applyNickName;
        }

        public ImageButton getAgreeButton() {
            return agreeButton;
        }

        public ImageButton getRefuseButton() {
            return refuseButton;
        }

        public View getItemView() {
            return itemView;
        }
    }
}
