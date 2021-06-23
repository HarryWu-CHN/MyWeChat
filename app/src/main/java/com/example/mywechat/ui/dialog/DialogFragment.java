package com.example.mywechat.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.mywechat.ui.Chat.ChatActivity;
import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.model.ChatRecord;
import com.example.mywechat.model.DialogRecord;
import com.example.mywechat.model.FriendRecord;
import com.example.mywechat.viewmodel.ChatSendViewModel;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DialogFragment extends Fragment {
    private FragmentManager fm;
    private ChatSendViewModel chatSendViewModel;
    private DialogAdapter dialogAdapter;
    private LinkedList<Dialog> data;
    private ListView listView;
    private String username;

    public DialogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogFragment newInstance() {
        return new DialogFragment();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        showActionBar(view);
        listView = getView().findViewById(R.id.listview);
        Context context = getActivity();

        username = ((App) requireActivity().getApplication()).getUsername();
        List<DialogRecord> dialogRecords = LitePal.findAll(DialogRecord.class);
        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        data = new LinkedList<>();
        for (DialogRecord dialogRecord : dialogRecords) {
            data.add(new Dialog(dialogRecord.getUniqueName(), dialogRecord.getNickName(), dialogRecord.getIconPath(), dialogRecord.getLastSpeak(), ""));
        }
        dialogAdapter = new DialogAdapter(data, context);
        listView.setAdapter(dialogAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = (Dialog) dialogAdapter.getItem(position);
                String nickname = dialog.getNickname();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", dialog.getUsername());
                bundle.putString("nickname", nickname);
                FriendRecord friendRecord = LitePal.where("friendName = ?", dialog.getUsername()).findFirst(FriendRecord.class);
                if (friendRecord != null) {
                    bundle.putString("sendToIcon", friendRecord.getIconPath());
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        chatSendViewModel = new ViewModelProvider(this).get(ChatSendViewModel.class);
        chatSendViewModel.getNewMsgLiveData().observe(getViewLifecycleOwner(), response ->{
            if (response == null) return;
            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String time = sdf.format(new Date().getTime());
            if (response.getInfoType() == 0) {
                Log.d("DialogFragment", "Receive New Msg" + response.toString());
                ChatRecord chatRecord2 = LitePal.where("userName = ? and friendName = ?", username, response.getFrom()).findFirst(ChatRecord.class);
                if (chatRecord2 == null) chatRecord2 = new ChatRecord(username, response.getFrom());
                chatRecord2.addAllYouNeed(response.getMsg(), response.getMsgType(), time, 0);
                chatRecord2.save();
                DialogRecord dialogRecord = LitePal.where("uniqueName = ?", response.getFrom()).findFirst(DialogRecord.class);
                if (dialogRecord == null) {
                    FriendRecord friendRecord = LitePal.where("friendName = ?", response.getFrom()).findFirst(FriendRecord.class);
                    if (friendRecord != null) {
                        Dialog dialog = new Dialog(response.getFrom(), friendRecord.getNickName(), friendRecord.getIconPath(), response.getMsg(), "");
                        dialogAdapter.addData(dialog);
                        dialogRecord = new DialogRecord(friendRecord.getFriendName(), friendRecord.getNickName(),
                                "0", friendRecord.getIconPath(), response.getMsg());
                    }
                    else return;
                } else {
                    dialogAdapter.updateData(response.getFrom(), response.getMsg());
                }
                dialogRecord.setLastSpeak(response.getMsg());
                dialogRecord.save();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    private void showActionBar(@NonNull View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        ((AppCompatActivity) context).getSupportActionBar().show();
    }

}