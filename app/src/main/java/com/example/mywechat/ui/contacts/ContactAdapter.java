package com.example.mywechat.ui.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {
    private LinkedList<Contact> data;

    public ContactAdapter() {

    }

    public ContactAdapter(LinkedList<Contact> data) {
        this.data = data;
    }

    public void setContacts(LinkedList<Contact> data) {
        this.data = data;
        notifyDataSetChanged();
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
        if (contact.getAvatarIcon() != null) {
            holder.getAvatar().setImageBitmap(contact.getAvatarIcon());
        }
        holder.getNickname().setText(contact.getNickName());

        holder.getItemView().setOnClickListener(v -> {
            if (contact.getAvatarIcon() == null) {
                return;
            }

            ByteArrayOutputStream iconBytes = new ByteArrayOutputStream();
            contact.getAvatarIcon().compress(Bitmap.CompressFormat.JPEG, 100, iconBytes);
            Intent intent = new Intent(holder.getItemView().getContext(), FriendActivity.class);
            intent.putExtra("userName", contact.getUserName());
            intent.putExtra("nickName", contact.getNickName());
            intent.putExtra("avatarBytes", iconBytes.toByteArray());
            holder.getItemView().getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ImageView contactAvatarView;
        private final TextView contactNicknameView;
        private final View itemView;

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

