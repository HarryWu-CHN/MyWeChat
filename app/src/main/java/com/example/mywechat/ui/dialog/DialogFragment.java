package com.example.mywechat.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mywechat.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class DialogFragment extends Fragment {

    private DialogAdapter chatAdapter;
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
        data.add(new Dialog(getString(R.string.nickname1), R.drawable.avatar1, getString(R.string.sentence1), "2021/01/01"));
        data.add(new Dialog(getString(R.string.nickname2), R.drawable.avatar2, getString(R.string.sentence2), "2021/01/02"));
        data.add(new Dialog(getString(R.string.nickname3), R.drawable.avatar3, getString(R.string.sentence3), "2021/01/03"));
        data.add(new Dialog(getString(R.string.nickname4), R.drawable.avatar4, getString(R.string.sentence4), "2021/01/04"));
        data.add(new Dialog(getString(R.string.nickname5), R.drawable.avatar5, getString(R.string.sentence5), "2021/01/05"));
        data.add(new Dialog(getString(R.string.nickname6), R.drawable.avatar6, getString(R.string.sentence6), "2021/01/06"));
        data.add(new Dialog(getString(R.string.nickname7), R.drawable.avatar7, getString(R.string.sentence7), "2021/01/07"));
        data.add(new Dialog(getString(R.string.nickname8), R.drawable.avatar8, getString(R.string.sentence8), "2021/01/08"));
        data.add(new Dialog(getString(R.string.nickname9), R.drawable.avatar9, getString(R.string.sentence9), "2021/01/09"));
        data.add(new Dialog(getString(R.string.nickname10), R.drawable.avatar10, getString(R.string.sentence10), "2021/01/10"));
        data.add(new Dialog(getString(R.string.nickname11), R.drawable.avatar11, getString(R.string.sentence11), "2021/01/11"));
        data.add(new Dialog(getString(R.string.nickname12), R.drawable.avatar12, getString(R.string.sentence12), "2021/01/12"));
        chatAdapter = new DialogAdapter(data, context);
        listView.setAdapter(chatAdapter);
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