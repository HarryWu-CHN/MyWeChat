package com.example.mywechat.Activities.Chat.chatFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mywechat.Activities.Chat.ChatActivity;
import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.model.ChatRecord;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.MessageType;
import com.example.mywechat.viewmodel.ChatSendViewModel;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        ChatActivity activity = (ChatActivity) getActivity();
        App app = (App) activity.getApplication();
        ChatRecord chatRecord = LitePal.where("userName = ? and friendName = ?", app.getUsername(), activity.getSendTo()).find(ChatRecord.class).get(0);
        List<String> msgs = chatRecord.getMsgs();
        List<String> msgTypes = chatRecord.getMsgTypes();
        List<String> times = chatRecord.getTimes();
        List<Boolean> isUser = chatRecord.getIsUser();
        data = new LinkedList<>();
        // TODO icon 变成从文件读入 bitmap格式
        for (int i=0; i<msgTypes.size(); i++) {
            data.add(new ChatBubble(times.get(i), msgs.get(i),
                    isUser.get(i) ? R.drawable.avatar5 : R.drawable.avatar6, isUser.get(i), msgTypes.get(i)));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(data);
        recyclerView.setAdapter(chatAdapter);
        // View bind
        textInputView = view.findViewById(R.id.textInput);
        sendBtn = view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(v -> {
            sendMsg(textInputView.getText().toString());
            textInputView.setText("");
        });
        // ViewModel bind
        chatSendViewModel = new ViewModelProvider(this).get(ChatSendViewModel.class);

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
        Log.d("Sending Msg:", msg);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String time = sdf.format(new Date().getTime());
        TextView topName = requireActivity().findViewById(R.id.topName);
        chatSendViewModel.chatSend(topName.getText().toString(), "0", msg, null);
        chatSendViewModel.getLiveData().observe(requireActivity(), response -> {
            if (response == null) {
                return;
            }
            if (response.component1()) {
                Message aMsg = new Message();
                aMsg.what = 0;
                aMsg.obj = new ChatBubble(time, msg, R.drawable.avatar5, true, Integer.valueOf(MessageType.TEXT.ordinal()).toString());
                handler.sendMessage(aMsg);
            }
        });
    }

    private Handler handler = new Handler(Looper.myLooper()) {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            // 通过Handler设置图片
            // 并正确处理 SUCCESS、NETWORK_ERROR、SERVER_ERROR 类型的消息
            // 提示：使用 setImageBitmap() 来将Bitmap对象显示到UI上
            switch (msg.what){
                case 0:
                    ChatBubble bubble = (ChatBubble) msg.obj;
                    chatAdapter.addData(data.size(), bubble);
                    break;
                default:
                    break;
            }

        }
    };

}