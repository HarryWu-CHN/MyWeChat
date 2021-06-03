package com.example.mywechat.ui.chatViewFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.FriendActivity;
import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.ContactAdapter;
import com.example.mywechat.ui.dialog.Dialog;

import java.util.LinkedList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private LinkedList<ChatBubble> data;

    public ChatAdapter(LinkedList<ChatBubble> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView;
        switch (viewType){
            case 0:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user2, parent, false);
                break;
            case 1:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user1, parent, false);
                break;
            default:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user2, parent, false);
                break;
        }
        return new ChatAdapter.ChatViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatBubble chatBubble = data.get(position);
        holder.getChatTimeView().setText(chatBubble.getTime());
        holder.getChatContentView().setText(chatBubble.getContent());
        holder.getChatIconView().setImageResource(chatBubble.getIcon());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return data.get(position).getIntMsgType();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addData(int position, ChatBubble bubble) {
        data.add(bubble);
        notifyItemInserted(position);
    }
    public void removeData(int position) {
        data.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView chatTimeView;
        private final TextView chatContentView;
        private final ImageView chatIconView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            this.chatTimeView = itemView.findViewById(R.id.chatTime);
            this.chatContentView = itemView.findViewById(R.id.chatContent);
            this.chatIconView = itemView.findViewById(R.id.chatIcon);
        }

        public TextView getChatTimeView() {
            return chatTimeView;
        }

        public TextView getChatContentView() {
            return chatContentView;
        }

        public ImageView getChatIconView() {
            return chatIconView;
        }
    }
}
