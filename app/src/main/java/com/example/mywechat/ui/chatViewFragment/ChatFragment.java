package com.example.mywechat.ui.chatViewFragment;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mywechat.R;
import com.example.mywechat.ui.contacts.ContactAdapter;
import com.example.mywechat.ui.dialog.Dialog;
import com.example.mywechat.ui.dialog.DialogAdapter;
import com.example.mywechat.ui.dialog.DialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private ChatAdapter chatAdapter;
    private LinkedList<ChatBubble> data;
    private RecyclerView recyclerView;


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
        Context context = getActivity();

        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        data = new LinkedList<>();
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
}