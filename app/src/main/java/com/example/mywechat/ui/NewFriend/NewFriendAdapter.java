package com.example.mywechat.ui.NewFriend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.LinkedList;

public class NewFriendAdapter extends RecyclerView.Adapter<NewFriendAdapter.ContactViewHolder> {
    private LinkedList<NewFriend> data;

    public NewFriendAdapter(LinkedList<NewFriend> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NewFriendAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_contact, parent, false);
        return new ContactViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        NewFriend newFriend = data.get(position);
        holder.getAvatar().setImageBitmap(newFriend.getAvatarIcon());
        holder.getNickname().setText(newFriend.getNickname());

        holder.getItemView().setOnClickListener(v -> {
            Intent intent = new Intent(holder.getItemView().getContext(), SendFriendApplyActivity.class);
            intent.putExtra("friendUserName", newFriend.getNickname());
            intent.putExtra("friendAvatar", newFriend.getAvatarIcon());
            holder.getItemView().getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{
        private final ImageView contactAvatarView;
        private final TextView contactNicknameView;
        private final View itemView;
        private int clickPosition;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            this.contactAvatarView = itemView.findViewById(R.id.contact_avatar);
            this.contactNicknameView = itemView.findViewById(R.id.contact_nickname);
            this.itemView = itemView;
        }

        public ImageView getAvatar() {
            return this.contactAvatarView;
        }

        public TextView getNickname() {
            return this.contactNicknameView;
        }

        public View getItemView() {
            return this.itemView;
        }
    }
}

