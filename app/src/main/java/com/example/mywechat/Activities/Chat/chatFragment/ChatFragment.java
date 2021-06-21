package com.example.mywechat.Activities.Chat.chatFragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mywechat.Activities.Chat.ChatActivity;
import com.example.mywechat.App;
import com.example.mywechat.FileUtil;
import com.example.mywechat.R;
import com.example.mywechat.model.ChatRecord;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.model.MessageType;
import com.example.mywechat.viewmodel.ChatSendViewModel;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

import static android.app.Activity.RESULT_OK;

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
    private ImageButton moreBtn;
    private String username;
    private String sendTo;

    private ChatSendViewModel chatSendViewModel;
    private ActivityResultLauncher<Integer> launcherImg;
    private ActivityResultLauncher<Integer> launcherVideo;


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

        ChatActivity activity = (ChatActivity) getActivity();
        App app = (App) activity.getApplication();
        username = app.getUsername();
        sendTo = activity.getSendTo();
        ChatRecord nChatRecord = new ChatRecord(username, sendTo);
        nChatRecord.save();

        List<ChatRecord> chatRecords = null;
        try {
            chatRecords = LitePal.where("userName = ? and friendName = ?", app.getUsername(), activity.getSendTo()).find(ChatRecord.class);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        data = new LinkedList<>();
        if (chatRecords != null && chatRecords.size() > 0) {
            ChatRecord chatRecord = chatRecords.get(0);
            List<String> msgs = chatRecord.getMsgs();
            List<String> msgTypes = chatRecord.getMsgTypes();
            List<String> times = chatRecord.getTimes();
            List<Boolean> isUser = chatRecord.getIsUser();
            // TODO icon 变成从文件读入 bitmap格式
            for (int i = 0; i < msgTypes.size(); i++) {
                data.add(new ChatBubble(times.get(i), msgs.get(i),
                        isUser.get(i) ? R.drawable.avatar5 : R.drawable.avatar6, isUser.get(i), msgTypes.get(i)));
            }
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
        moreBtn = (ImageButton) view.findViewById(R.id.moreBtn);
        moreBtn.setOnClickListener(v -> {
            setDialog();
        });
        // ViewModel bind
        chatSendViewModel = new ViewModelProvider(this).get(ChatSendViewModel.class);
        chatSendViewModel.getLiveData().observe(requireActivity(), response -> {
            if (response == null) {
                return;
            }
            if (response.component1()) {
                SimpleDateFormat sdf =new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                String time = sdf.format(response.component2());
                ChatBubble bubble = null;
                switch (response.getMsgType()) {
                    case "0":
                        bubble = new ChatBubble(time, response.getMsg(), R.drawable.avatar5, true, Integer.valueOf(MessageType.TEXT.ordinal()).toString());
                        break;
                    case "1":
                        Log.d("LiveData Observe Img", Objects.requireNonNull(response.getFile()).getPath());
                        Bitmap bitmap = BitmapFactory.decodeFile(Objects.requireNonNull(response.getFile()).getPath());
                        bubble = new ChatBubble(time, bitmap, R.drawable.avatar5, true, Integer.valueOf(MessageType.IMAGE.ordinal()).toString());
                        break;
                    case "2":
                        Log.d("LiveData Observe Video", Objects.requireNonNull(response.getFile()).getPath());
                        bubble = new ChatBubble(time, Objects.requireNonNull(response.getFile()).getPath(), R.drawable.avatar5, true, Integer.valueOf(MessageType.VIDEO.ordinal()).toString());
                        break;
                }
                ChatRecord chatRecord = LitePal.where("userName = ? and friendName = ?", app.getUsername(), activity.getSendTo()).findFirst(ChatRecord.class);
                chatRecord.addAllYouNeed(response.getMsg(), response.getMsgType(), time, true);
                chatRecord.save();
                if (bubble != null)
                    chatAdapter.addData(data.size(), bubble);
            }
        });
        // launcher
        launcherImg = registerForActivityResult(new ResultContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String imagePath) {
                if (imagePath == null) return;
                sendImg(imagePath);
            }
        });
        launcherVideo = registerForActivityResult(new ResultContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String videoPath) {
                if (videoPath == null) return;
                sendVideo(videoPath);
            }
        });
        /* TODO
        videoView.requestFocus();
        videoView.start();
         */
    }

    private void setDialog() {
        Dialog bottom_dialog = new Dialog(getActivity(), R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.bottom_dialog_chat, null);
        //初始化视图
        root.findViewById(R.id.btn_img).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcherImg.launch(0);
            bottom_dialog.dismiss();
        });
        root.findViewById(R.id.btn_video).setOnClickListener(v -> {
            launcherVideo.launch(1);
            bottom_dialog.dismiss();
        });
        root.findViewById(R.id.btn_location).setOnClickListener(v -> {

        });
        bottom_dialog.setContentView(root);
        Window dialogWindow = bottom_dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
//        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        bottom_dialog.show();
    }

    class ResultContract extends ActivityResultContract<Integer, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer requestCode) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (requestCode.equals(0)) {
                intent.setType("image/*");
            } else if (requestCode.equals(1)){
                intent.setType("video/*");
            }
            return intent;
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK && intent != null)
                return FileUtil.handleStorageImage(getActivity(), intent);
            return null;
        }
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
    }

    public void sendImg(String imagePath) {
        Log.d("Sending Img", imagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        File file = new File(imagePath);
        chatSendViewModel.chatSend(sendTo, "1", null, file);
    }

    public void sendVideo(String videoPath) {
        File file = new File(videoPath);
        chatSendViewModel.chatSend(sendTo, "2", null, file);
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