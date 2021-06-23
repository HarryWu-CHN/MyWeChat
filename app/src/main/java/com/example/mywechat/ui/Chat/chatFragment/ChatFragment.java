package com.example.mywechat.ui.Chat.chatFragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mywechat.ui.Chat.ChatActivity;
import com.example.mywechat.App;
import com.example.mywechat.BuildConfig;
import com.example.mywechat.R;
import com.example.mywechat.Util.FileUtil;
import com.example.mywechat.api.ChatRecordBody;
import com.example.mywechat.model.ChatRecord;
import com.example.mywechat.model.MessageType;
import com.example.mywechat.model.UserInfo;
import com.example.mywechat.viewmodel.ChatSendViewModel;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

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
    private Bitmap userIcon;
    private Bitmap friendIcon;
    private File curImageFile;
    static String FILE_LOAD_PRE = "http://8.140.133.34:7262/";

    private ChatSendViewModel chatSendViewModel;
    private ActivityResultLauncher<Integer> launcherImg;
    private ActivityResultLauncher<Integer> launcherVideo;
    private ActivityResultLauncher<Integer> launcherPicCapture;
    private ActivityResultLauncher<Integer> launcherVidCapture;
    private ActivityResultLauncher<Integer> launcherMicCapture;

    private ImageView previewImage;
    private VideoView previewVideo;
    private MediaPlayer previewMedia;
    private Dialog previewDialog;

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

        initPreviewDialog();

        ChatActivity activity = (ChatActivity) getActivity();
        App app = (App) activity.getApplication();
        username = app.getUsername();
        sendTo = activity.getSendTo();
        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        data = new LinkedList<>();
        /* TODO 先不做本地数据库
        if (chatRecord != null) {
            List<String> msgs = chatRecord.getMsgs();
            List<String> msgTypes = chatRecord.getMsgTypes();
            List<String> times = chatRecord.getTimes();
            List<Integer> isUser = chatRecord.getIsUser();
            Log.d("debug", isUser.get(0).getClass().toString());

            for (int i = 0; i < msgTypes.size(); i++) {
                data.add(new ChatBubble(times.get(i), msgs.get(i),
                        isUser.get(i).equals(1) ? R.drawable.avatar5 : R.drawable.avatar6, isUser.get(0).equals(1), msgTypes.get(i)));
            }
        }
         */
        // TODO 获取对应的icon
        UserInfo userInfo = LitePal.where("username = ?", username).findFirst(UserInfo.class);
        if (userInfo != null) {
            String userIconPath = userInfo.getUserIcon();
            userIcon = BitmapFactory.decodeFile(userIconPath);
        } else {
            userIcon = null;
        }
        friendIcon = ((ChatActivity)requireActivity()).getFriendBitmap();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        // recyclerView 相关绑定
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter = new ChatAdapter(data, (Context)requireActivity());
        recyclerView.setAdapter(chatAdapter);
        // 气泡点击事件
        chatAdapter.setOnItemClickListener((v, position) -> {
            Log.d("ItemClicked", "~~~!!!~~~");
            ChatBubble bubble = data.get(position);
            switch (bubble.getIntMsgType()) {
                case 1:
                    // Click image
                    previewImage.setImageBitmap((Bitmap) bubble.getContent());
                    previewDialog.setContentView(previewImage);
                    previewDialog.show();
                    break;
                case 2:
                    // Click video
                    previewVideo.setVideoURI(Uri.parse((String) bubble.getContent()));
                    previewDialog.setContentView(previewVideo);
                    previewDialog.show();
                    previewVideo.start();
                    break;
                case 3:
                    // Click location
                    String loc = (String) bubble.getContent();
                    // 对地址进行解析并传递给新创建的intent.
                    Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
                    Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
                    if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Log.d("ImplicitIntents", "Can't handle this intent!");
                    }
                    break;
                case 4:
                    // Click media
                    previewMedia = new MediaPlayer();
                    try {
                        previewMedia.setDataSource(getActivity(), Uri.parse((String) bubble.getContent()));
                        previewMedia.prepare();
                    } catch (IOException e) {
                        // ignored
                    }
                    previewMedia.start();
                    break;
                default:
                    // ignored
                    break;
            }
        });
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
        chatSendViewModel.chatRecordGet(sendTo);
        chatSendViewModel.getChatGetLiveDate().observe(requireActivity(), response -> {
            if (response == null || !response.component1()) {
                return;
            }
            List<ChatRecordBody> chatRecords = response.getRecordList();
            pushBubbles(chatRecords);
            List<String> msgs = new ArrayList<>();
            List<String> msgTypes = new ArrayList<>();
            List<String> times = new ArrayList<>();
            List<Integer> isUser = new ArrayList<>();
            for (ChatRecordBody body : chatRecords) {
                msgs.add(body.getContent());
                String msgEnumIndex = Integer.valueOf( MessageType.valueOf(body.getMessageType()).ordinal() ).toString();
                msgTypes.add(msgEnumIndex);
                times.add(body.getTime());
                isUser.add(body.getSenderName().equals(username)? 1 : 0);
            }
            ChatRecord chatRecord1 = LitePal.where("userName = ? and friendName = ?", username, sendTo).findFirst(ChatRecord.class);
            if (chatRecord1 == null) chatRecord1 = new ChatRecord(username, sendTo);
            chatRecord1.setMsgs(msgs);   chatRecord1.setMsgTypes(msgTypes);
            chatRecord1.setTimes(times); chatRecord1.setIsUser(isUser);
            chatRecord1.save();
        });
        // 发送消息成功的回调
        chatSendViewModel.getLiveData().observe(requireActivity(), response -> {

            if (response == null || !response.component1()) {
                return;
            }
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String time = sdf.format(response.component2());
            ChatBubble bubble = null;
            switch (response.getMsgType()) {
                case "0":
                    bubble = new ChatBubble(time, response.getMsg(), userIcon, true, Integer.valueOf(MessageType.TEXT.ordinal()).toString());
                    break;
                case "1":
                    Log.d("LiveData Observe Img", Objects.requireNonNull(response.getFile()).getPath());
                    Bitmap bitmap = BitmapFactory.decodeFile(Objects.requireNonNull(response.getFile()).getPath());
                    bubble = new ChatBubble(time, bitmap, userIcon, true, Integer.valueOf(MessageType.PICTURE.ordinal()).toString());
                    break;
                case "2":
                    Log.d("LiveData Observe Video", Objects.requireNonNull(response.getFile()).getPath());
                    bubble = new ChatBubble(time, Objects.requireNonNull(response.getFile()).getPath(), userIcon, true, Integer.valueOf(MessageType.VIDEO.ordinal()).toString());
                    break;
                case "3":
                    Log.d("LiveData Location", response.getMsg());
                    bubble = new ChatBubble(time, response.getMsg(), userIcon, true, "3");
                    break;
                case "4":
                    Log.d("LiveData Sound", response.getMsg());
                    bubble = new ChatBubble(time, response.getMsg(), userIcon, true, "4");
                    break;
            }
            ChatRecord chatRecord2 = LitePal.where("userName = ? and friendName = ?", username, sendTo).findFirst(ChatRecord.class);
            chatRecord2.addAllYouNeed(response.getMsg(), response.getMsgType(), time, 1);
            chatRecord2.save();
            if (bubble != null)
                chatAdapter.addData(data.size(), bubble);
        });
        // 接收到WebSocket发来的新消息
        chatSendViewModel.getNewMsgLiveData().observe(requireActivity(), response -> {
            if (response == null) return;
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String time = sdf.format(new Date().getTime());
            if (response.getInfoType() == 0 && response.getFrom().equals(sendTo)) {
                ChatBubble bubble = null;
                switch (response.getMsgType()) {
                    case "0":
                        bubble = new ChatBubble(time, response.getMsg(), friendIcon, false, Integer.valueOf(MessageType.TEXT.ordinal()).toString());
                        chatAdapter.addData(data.size(), bubble);
                        break;
                    case "1":
                        getWebImg(time, response.getMsg(), friendIcon, false);
                        break;
                    case "2":
                        String path = FILE_LOAD_PRE + response.getMsg();
                        bubble = new ChatBubble(time, path, friendIcon, false, "2");
                        chatAdapter.addData(data.size(), bubble);
                        break;
                    case "3":
                        path = FILE_LOAD_PRE + response.getMsg();
                        bubble = new ChatBubble(time, path, friendIcon, false, "3");
                        chatAdapter.addData(data.size(), bubble);
                        break;
                    case "4":
                        path = FILE_LOAD_PRE + response.getMsg();
                        bubble = new ChatBubble(time, path, userIcon, false, "4");
                        chatAdapter.addData(data.size(), bubble);
                        break;
                }
            }
            if (response.getInfoType() == 0) {
                Log.d("ChatFragment", "Receive New Msg" + response.toString());
                ChatRecord chatRecord2 = LitePal.where("userName = ? and friendName = ?", username, sendTo).findFirst(ChatRecord.class);
                chatRecord2.addAllYouNeed(response.getMsg(), response.getMsgType(), time, 0);
                chatRecord2.save();
            }
        });
        // 发送图片
        launcherImg = registerForActivityResult(new ResultContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String imagePath) {
                if (imagePath == null) return;
                sendImg(imagePath);
            }
        });
        // 发送视频
        launcherVideo = registerForActivityResult(new ResultContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String videoPath) {
                if (videoPath == null) return;
                sendVideo(videoPath);
            }
        });
        // 拍摄照片
        launcherPicCapture = registerForActivityResult(new ResultContractCapture(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String result) {
                if (result == null) return;
                sendImg(result);
            }
        });
        launcherVidCapture = registerForActivityResult(new ResultContractCapture(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String result) {
                if (result == null) return;
                sendVideo(result);
            }
        });
        launcherMicCapture = registerForActivityResult(new ResultContractRecording(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String result) {
                if (result == null) return;
                Log.d("On Ac Result", result);
                sendRecording(result);
            }
        });
        /* TODO
        videoView.requestFocus();
        videoView.start();
         */
    }

    private void initPreviewDialog() {
        // 预览Dialog
        previewDialog = new Dialog(getActivity(), R.style.FullActivity);
        WindowManager.LayoutParams attributes = previewDialog.getWindow().getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
        //attributes.gravity = Gravity.CENTER_VERTICAL;
        previewDialog.getWindow().setAttributes(attributes);

        previewImage = new ImageView(getActivity());
        previewImage.setOnClickListener(v -> {
            previewDialog.dismiss();
        });

        previewVideo = new VideoView(getActivity());
//        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.WRAP_CONTENT);
//        LayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
//        previewVideo.setLayoutParams(LayoutParams);
        previewVideo.setOnClickListener(v -> {
            previewDialog.dismiss();
        });
    }

    class ResultContract extends ActivityResultContract<Integer, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer requestCode) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            switch (requestCode) {
                case 0:
                    intent.setType("image/*");
                    break;
                case 1:
                    intent.setType("video/*");
                    break;
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

    class ResultContractCapture extends ActivityResultContract<Integer, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer requestCode) {
            // 调用相机拍照
            File dir = new File(Environment.getExternalStorageDirectory(),"MyWeChat");
            if (!dir.exists()) dir.mkdirs();
            String storagePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "MyWeChat";
            dir = new File(storagePath);
            dir.mkdirs();
            String uuid = UUID.randomUUID().toString();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);;
            if (requestCode == 2) {
                storagePath = storagePath + File.separator + "pictures";
                dir = new File(storagePath);    dir.mkdirs();
                curImageFile = new File(dir, uuid + ".jpg");
            } else if (requestCode == 3) {
                storagePath = storagePath + File.separator + "video";
                dir = new File(storagePath);    dir.mkdirs();
                curImageFile = new File(dir, uuid + ".mp4");
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            } else if (requestCode == 4) {
                storagePath = storagePath + File.separator + "sound";
                dir = new File(storagePath);    dir.mkdirs();
                curImageFile = new File(dir, uuid + ".amr");
                intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            }
            if (curImageFile.exists()) return null;
            try {
                curImageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri photoUri = FileProvider.getUriForFile(
                    requireActivity(),
                    BuildConfig.APPLICATION_ID + ".provider",
                    curImageFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            return intent;
        }
        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK) {
                Log.d("Take Photo", curImageFile.getAbsolutePath());
                return curImageFile.getAbsolutePath();
            }
            return null;
        }
    }
    class ResultContractRecording extends ActivityResultContract<Integer, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Integer input) {
            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        }
        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == RESULT_OK && intent != null) {
                return FileUtil.handleStorageImage(getActivity(), intent);
            }
            return null;
        }
    }

    private void setDialog() {
        Dialog bottom_dialog = new Dialog(getActivity(), R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.bottom_dialog_chat, null);
        // 初始化视图
        // 按钮点击事件
        root.findViewById(R.id.btn_pic_capture).setOnClickListener(v -> {
            launcherPicCapture.launch(2);
            bottom_dialog.dismiss();
        });
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
            sendLocation(textInputView.getText().toString());
            textInputView.setText("");
            bottom_dialog.dismiss();
        });
        root.findViewById(R.id.btn_vid_capture).setOnClickListener(v -> {
            launcherVidCapture.launch(3);
            bottom_dialog.dismiss();
        });
        root.findViewById(R.id.btn_microphone).setOnClickListener(v -> {
            launcherMicCapture.launch(4);
            bottom_dialog.dismiss();
        });
        bottom_dialog.setContentView(root);
        Window dialogWindow = bottom_dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);

        // dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
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
        chatSendViewModel.chatSend(sendTo, "0", msg, null);
    }
    public void sendImg(String imagePath) {
        Log.d("Sending Img", imagePath);
        File file = new File(imagePath);
        chatSendViewModel.chatSend(sendTo, "1", imagePath, file);
    }
    public void sendVideo(String videoPath) {
        File file = new File(videoPath);
        chatSendViewModel.chatSend(sendTo, "2", videoPath, file);
    }
    public void sendLocation(String location) {
        Log.d("Sending Location:", location);
        chatSendViewModel.chatSend(sendTo, "3", location, null);
    }
    public void sendRecording(String result) {
        File file = new File(result);
        chatSendViewModel.chatSend(sendTo, "4", result, file);
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

    public void pushBubbles(List<ChatRecordBody> chatRecords) {
        new Thread(() -> {
            for (ChatRecordBody body : chatRecords) {
                boolean isuser = body.getSenderName().equals(username);
                ChatBubble bubble = null;
                switch (body.getMessageType()) {
                    case "TEXT":
                        bubble = new ChatBubble(body.getTime(), body.getContent() == null ? "" : body.getContent(),
                                isuser ? userIcon : friendIcon, isuser, Integer.valueOf(MessageType.TEXT.ordinal()).toString());
                        break;
                    case "PICTURE":
                        String path = "http://8.140.133.34:7262/" + body.getContent();
                        try {
                            URL url = new URL(path);
                            Bitmap bitmap = null;
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(10000);
                            //获取返回码
                            int code = connection.getResponseCode();
                            if (code == 200) {
                                InputStream inputStream = connection.getInputStream();
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                inputStream.close();
                            }
                            bubble = new ChatBubble(body.getTime(), bitmap, isuser ? userIcon : friendIcon, isuser, "1");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "VIDEO":
                        path = "http://8.140.133.34:7262/" + body.getContent();
                        bubble = new ChatBubble(body.getTime(), path, isuser ? userIcon : friendIcon, isuser, "2");
                        break;
                    case "POSITION":
                        bubble = new ChatBubble(body.getTime(), body.getContent(), isuser ? userIcon : friendIcon, isuser, "3");
                        break;
                    case "SOUND":
                        bubble = new ChatBubble(body.getTime(), FILE_LOAD_PRE+body.getContent(), isuser ? userIcon : friendIcon, isuser, "4");
                        break;
                }
                if (bubble != null) {
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = bubble;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void getWebImg(String time, String imgPath, Bitmap avatar, boolean isUser) {
        new Thread(() -> {
            String path = "http://8.140.133.34:7262/" + imgPath;
            try {
                URL url = new URL(path);
                Bitmap bitmap = null;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                //获取返回码
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream inputStream = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                }
                Message msg = new Message();
                msg.what = 0;
                msg.obj = new ChatBubble(time, bitmap, avatar, isUser, "1");
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}