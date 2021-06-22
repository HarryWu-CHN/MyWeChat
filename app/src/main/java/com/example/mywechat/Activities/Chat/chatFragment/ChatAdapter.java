package com.example.mywechat.Activities.Chat.chatFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private LinkedList<ChatBubble> data;
    private OnItemClickListener mOnItemClickListener;
    private Context context;

    public ChatAdapter(LinkedList<ChatBubble> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = null;
        switch (viewType) {
            case -1:
            case -4:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user1, parent, false);
                break;
            case -2:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_img_user1, parent, false);
                break;
            case 1:
            case 4:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user2, parent, false);
                break;
            case 2:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_img_user2, parent, false);
                break;
            case 3:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_vid_user2, parent, false);
                break;
            case -3:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_vid_user1, parent, false);
                break;
            case -5:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sound_user1, parent, false);
                break;
            case 5:
                mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sound_user2, parent, false);
                break;
            default:
                break;
        }

        return new ChatAdapter.ChatViewHolder(mView, Math.abs(viewType)-1);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        ChatBubble chatBubble = data.get(position);
        switch (chatBubble.getIntMsgType()) {
            case 0:
                holder.getChatContentView().setText((String) chatBubble.getContent());
                break;
            case 1:
                holder.getChatImgView().setImageBitmap((Bitmap) chatBubble.getContent());
                break;
            case 2:
                String filePath = (String)chatBubble.getContent();
                if (filePath.contains("http://8.140")) {
                    Uri uri = Uri.parse(filePath);
                    holder.getChatVidView().setVideoURI(uri);
                } else {
                    holder.getChatVidView().setVideoPath(filePath);
                }
                holder.getChatVidView().seekTo(100);
                break;
            case 3:
                TextView txView = holder.getChatContentView();
                txView.setText((String) chatBubble.getContent());
                txView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                txView.getPaint().setAntiAlias(true);
                break;
            case 4:
                filePath = (String) chatBubble.getContent();
                MediaPlayer mediaPlayer = new MediaPlayer();
                int soundSecond;
                try {
                    if (filePath.contains("http://8.140")) {
                        mediaPlayer.setDataSource(context, Uri.parse(filePath));
                    } else {
                        mediaPlayer.setDataSource(filePath);
                    }
                    mediaPlayer.prepare();
                    soundSecond = mediaPlayer.getDuration() / 1000;
                    String secondStr = soundSecond + "'";
                    holder.getChatContentView().setText(secondStr);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
        }
        holder.getChatTimeView().setText(chatBubble.getTime());
        holder.getChatIconView().setImageBitmap(chatBubble.getIcon());

        if (mOnItemClickListener != null) {
            holder.contentView.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, position));
        }
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
        private TextView chatContentView;
        private final ImageView chatIconView;
        private ImageView chatImgView;
        private VideoView chatVidView;
        View contentView;

        public ChatViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.chatTimeView = itemView.findViewById(R.id.chatTime);
            this.chatIconView = itemView.findViewById(R.id.chatIcon);
            switch (viewType) {
                case 0:
                case 3:
                case 4:
                    this.chatContentView = itemView.findViewById(R.id.chatContent);
                    break;
                case 1:
                    this.chatImgView = itemView.findViewById(R.id.chatContent);
                    break;
                case 2:
                    this.chatVidView = itemView.findViewById(R.id.chatContent);
                    break;
            }
            contentView = itemView.findViewById(R.id.chatContent);
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
        public ImageView getChatImgView() {
            return chatImgView;
        }
        public VideoView getChatVidView() { return chatVidView; }
    }
}
