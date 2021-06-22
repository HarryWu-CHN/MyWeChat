package com.example.mywechat.ui.Group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.Contact;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.InviteHolder> {
    private LinkedList<Contact> invite;
    private  Map<Integer, Boolean> map = new HashMap<>();

    public InviteAdapter(LinkedList<Contact> invite) {
        this.invite = invite;
        initMap();
    }

    private void initMap() {
        for (int i = 0; i < invite.size(); i++) {
            map.put(i, false);
        }
    }
    public Map<Integer, Boolean> getMap() {
        return map;
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

        holder.getFriendNickName().setText(contact.getNickName());
        holder.getFriendAvatar().setImageBitmap(contact.getAvatarIcon());
        // checkbox 监听
        holder.getCheck().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用map集合保存
                map.put(position, isChecked);
            }
        });
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.getCheck().setChecked(map.get(position));
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
