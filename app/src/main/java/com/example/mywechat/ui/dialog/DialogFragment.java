package com.example.mywechat.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.mywechat.Activities.Chat.ChatActivity;
import com.example.mywechat.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class DialogFragment extends Fragment {
    private FragmentManager fm;
    private DialogAdapter dialogAdapter;
    private LinkedList<Dialog> data;
    private ListView listView;

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

        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        data = new LinkedList<>();
        data.add(new Dialog("b", "b", R.drawable.avatar1, getString(R.string.sentence1), "2021/01/01"));
        dialogAdapter = new DialogAdapter(data, context);
        listView.setAdapter(dialogAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog dialog = (Dialog) dialogAdapter.getItem(position);
                String nickname = dialog.getNickname();
                int icon = dialog.getAvatarIcon();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", dialog.getUsername());
                bundle.putString("nickname", nickname);
                bundle.putInt("icon", icon);
                intent.putExtras(bundle);
                startActivity(intent);
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