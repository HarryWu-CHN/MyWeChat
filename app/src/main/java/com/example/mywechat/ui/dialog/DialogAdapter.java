package com.example.mywechat.ui.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mywechat.R;

import java.util.LinkedList;

public class DialogAdapter extends BaseAdapter {

    private LinkedList<Dialog> data;
    private Context context;

    public DialogAdapter(LinkedList<Dialog> data, Context context) {
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.item_list_dialog, parent, false);
        Dialog chat = data.get(position);
        // 修改View中各个控件的属性，使之显示对应位置Chat的内容
        // 使用convertView.findViewById()方法来寻找对应的控件
        // 控件ID见 res/layout/item_list_chat.xml

        TextView nicknameView = convertView.findViewById(R.id.nickname_text);
        nicknameView.setText(chat.getNickname());
        ImageView avatarIconView = convertView.findViewById(R.id.avatar_icon);
        avatarIconView.setImageResource(chat.getAvatarIcon());
        TextView lastSpeakView = convertView.findViewById(R.id.last_speak_text);
        lastSpeakView.setText(chat.getLastSpeak());
        TextView lastSpeakTimeView = convertView.findViewById(R.id.last_speak_time_text);
        lastSpeakTimeView.setText(chat.getLastSpeakTime());
        return convertView;
    }
}
