package com.example.mywechat.Activities.NewFriend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import java.util.LinkedList;

public class FriendApplyAdapter extends RecyclerView.Adapter<FriendApplyAdapter.ApplyViewHolder>{
    private LinkedList<NewFriend> data;

    public FriendApplyAdapter(LinkedList<NewFriend> data) {
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
        NewFriend friendApply = data.get(position);
        holder.getApplyAvatar().setImageBitmap(friendApply.getAvatarIcon());
        holder.getApplyNickName().setText(friendApply.getNickname());

        holder.getAgreeButton().setOnClickListener(v -> {
            // TODO: 同意申请
            holder.getNfViewModel().contactAgree(friendApply.getNickname(), true);

            holder.getAgreeButton().setVisibility(View.GONE);
            holder.getRefuseButton().setVisibility(View.GONE);
            holder.getResultText().setVisibility(View.VISIBLE);
            holder.getResultText().setText("已同意");
        });

        holder.getRefuseButton().setOnClickListener(v -> {
            // TODO: 拒绝申请
            holder.getNfViewModel().contactAgree(friendApply.getNickname(), false);

            holder.getAgreeButton().setVisibility(View.GONE);
            holder.getRefuseButton().setVisibility(View.GONE);
            holder.getResultText().setVisibility(View.VISIBLE);
            holder.getResultText().setText("已拒绝");
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
        private TextView resultText;
        private View itemView;
        private NewFriendViewModel NfViewModel;

        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);

            this.applyAvatar = itemView.findViewById(R.id.applyAvatar);
            this.applyNickName = itemView.findViewById(R.id.applyNickname);
            this.agreeButton = itemView.findViewById(R.id.agreeButton);
            this.refuseButton = itemView.findViewById(R.id.refuseButton);
            this.resultText = itemView.findViewById(R.id.resultText);
            this.itemView = itemView;

            NfViewModel = new ViewModelProvider((AppCompatActivity) itemView.getContext())
                    .get(NewFriendViewModel.class);
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

        public TextView getResultText() {
            return resultText;
        }

        public NewFriendViewModel getNfViewModel() {
            return NfViewModel;
        }

        public View getItemView() {
            return itemView;
        }
    }
}
