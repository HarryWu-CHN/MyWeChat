package com.example.mywechat.ui.chatViewFragment;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.ContactAdapter;
import com.example.mywechat.ui.dialog.Dialog;
import com.example.mywechat.ui.dialog.DialogAdapter;
import com.example.mywechat.ui.dialog.DialogFragment;
import com.example.mywechat.viewmodel.ChatSendViewModel;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class ChatFragment extends Fragment {
    private ChatAdapter chatAdapter;
    private LinkedList<ChatBubble> data;
    private RecyclerView recyclerView;
    private TextView textInputView;
    private Button sendBtn;

    private ChatSendViewModel chatSendViewModel;


    public ChatFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.chatList);
        chatSendViewModel = new ViewModelProvider(this).get(ChatSendViewModel.class);
        data = new LinkedList<>();
        textInputView = view.findViewById(R.id.textInput);
        sendBtn = view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(v -> {
            sendMsg(textInputView.getText().toString());
        });

        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        data.add(new ChatBubble("2021/01/01", getString(R.string.sentence1), R.drawable.avatar5,MsgType.USER_TEXT));
        data.add(new ChatBubble("2021/12/01", getString(R.string.paragraph2), R.drawable.avatar6,MsgType.RCV_TEXT));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ChatAdapter(data));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    public void sendMsg(@NotNull String msg) {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String time = sdf.format(new Date().getTime());
        data.add(new ChatBubble(time, msg, R.drawable.avatar5, MsgType.USER_TEXT));
        recyclerView.setAdapter(new ChatAdapter(data));
    }
}