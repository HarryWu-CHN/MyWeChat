package com.example.mywechat.ui.Group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.Contact;

import java.util.LinkedList;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteHolder> {
    private LinkedList<Contact> invite;

    public InviteAdapter(LinkedList<Contact> invite) {
        this.invite = invite;
    }

    @NonNull
    @Override
    public InviteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_invite, parent, false);
        return new InviteHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull InviteHolder holder, int position) {
        Contact contact = invite.get(position);

        holder.getFriendNickName().setText(contact.getNickname());
        holder.getFriendAvatar().setImageBitmap(contact.getAvatarIcon());
    }

    @Override
    public int getItemCount() {
        return invite.size();
    }

    public class InviteHolder extends RecyclerView.ViewHolder {
        private ImageView friendAvatar;
        private TextView friendNickName;
        private CheckBox check;

        public InviteHolder(@NonNull View itemView) {
            super(itemView);

            friendAvatar = itemView.findViewById(R.id.friend_avatar);
            friendNickName = itemView.findViewById(R.id.friend_nickname);
            check = itemView.findViewById(R.id.inviteCheck);
        }

        public ImageView getFriendAvatar() {
            return friendAvatar;
        }

        public TextView getFriendNickName() {
            return friendNickName;
        }

        public CheckBox getCheck() {
            return check;
        }
    }
}
