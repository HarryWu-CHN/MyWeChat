package com.example.mywechat.ui.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.LinkedList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private LinkedList<Contact> data;

    // 完成类ContactViewHolder
    // 使用itemView.findViewById()方法来寻找对应的控件
    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView contactAvatarView;
        private final TextView contactNicknameView;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            this.contactAvatarView = itemView.findViewById(R.id.contact_avatar);
            this.contactNicknameView = itemView.findViewById(R.id.contact_nickname);
        }

        public ImageView getAvatar() {
            return this.contactAvatarView;
        }

        public TextView getNickname() {
            return this.contactNicknameView;
        }

    }

    public ContactAdapter(LinkedList<Contact> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_contact, parent, false);
        return new ContactViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = data.get(position);
        holder.getAvatar().setImageResource(contact.getAvatarIcon());
        holder.getNickname().setText(contact.getNickname());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

