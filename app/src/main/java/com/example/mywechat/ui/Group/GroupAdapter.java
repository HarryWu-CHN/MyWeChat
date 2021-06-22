package com.example.mywechat.ui.Group;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {
    private ArrayList<Group> groups;

    public GroupAdapter() {

    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycle_contact, parent, false);
        return new GroupViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        Group group = this.groups.get(position);

        holder.getGroupName().setText(group.getGroupName());
        if (group.getCreatorIcon() != null) {
            holder.getGroupAvatar().setImageBitmap(group.getCreatorIcon());
        }

        holder.getItemView().setOnClickListener(v -> {
            Intent intent = new Intent(holder.getItemView().getContext(), GroupActivity.class);
            intent.putExtra("groupId", group.getGroupId());
            intent.putExtra("groupName", group.getGroupName());
            holder.getItemView().getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (groups == null) {
            return 0;
        }
        return groups.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        private final ImageView groupAvatar;
        private final TextView groupName;
        private final View itemView;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);

            this.groupAvatar = itemView.findViewById(R.id.contact_avatar);
            this.groupName = itemView.findViewById(R.id.contact_nickname);
            this.itemView = itemView;
        }

        public ImageView getGroupAvatar() {
            return groupAvatar;
        }

        public TextView getGroupName() {
            return groupName;
        }

        public View getItemView() {
            return itemView;
        }
    }
}
