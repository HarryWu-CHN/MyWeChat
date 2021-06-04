package com.example.mywechat.Activities.Chat.chatFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.util.LinkedList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private LinkedList<ChatBubble> data;

    public ChatAdapter(LinkedList<ChatBubble> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = null;
        if (viewType < 0) {
            switch (viewType) {
                case -1:
                    mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user1, parent, false);
                    break;
                case -2:
                    break;
                default:
                    break;
            }
        } else {
            switch (viewType) {
                case 1:
                    mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user2, parent, false);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
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
        if (!data.get(position).isUser()) {
            return -data.get(position).getIntMsgType()-1;
        } else {
            return data.get(position).getIntMsgType()+1;
        }
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
