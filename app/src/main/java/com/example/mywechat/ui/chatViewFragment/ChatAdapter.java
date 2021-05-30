package com.example.mywechat.ui.chatViewFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mywechat.R;
import com.example.mywechat.ui.dialog.Dialog;

import java.util.LinkedList;

public class ChatAdapter extends BaseAdapter {
    private LinkedList<ChatBubble> data;
    private Context context;

    public ChatAdapter(LinkedList<ChatBubble> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatBubble chatBubble = data.get(position);
        if (chatBubble.isSpeaker())
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_user2, parent, false);
        else
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chat_user1, parent, false);
        TextView chatTimeView = convertView.findViewById(R.id.chatTime);
        chatTimeView.setText(chatBubble.getTime());
        TextView chatContentView = convertView.findViewById(R.id.chatContent);
        chatContentView.setText(chatBubble.getContent());
        return convertView;
    }
}
