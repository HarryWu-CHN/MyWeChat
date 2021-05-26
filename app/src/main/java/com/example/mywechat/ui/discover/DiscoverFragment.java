package com.example.mywechat.ui.discover;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.NewDiscoverActivity;
import com.example.mywechat.R;
import com.example.mywechat.ui.comment.Comment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedList;

public class DiscoverFragment extends Fragment {
    private RecyclerView recyclerView;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExploreFragment.
     */
    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        hideActionBar(view);
        recyclerView = view.findViewById(R.id.discover_recyclerview);

        // 添加数据
        LinkedList<Discover> discovers = new LinkedList<>();
        ArrayList<Integer> imageList1 = new ArrayList<>();
        ArrayList<Integer> imageList2 = new ArrayList<>();
        ArrayList<Integer> imageList3 = new ArrayList<>();
        ArrayList<Integer> imageList4 = new ArrayList<>();
        ArrayList<Integer> imageList5 = new ArrayList<>();
        ArrayList<Integer> imageList6 = new ArrayList<>();

        imageList2.add(R.drawable.image1);

        imageList3.add(R.drawable.image2);
        imageList3.add(R.drawable.image3);

        imageList4.add(R.drawable.image4);
        imageList4.add(R.drawable.image5);
        imageList4.add(R.drawable.image6);

        imageList5.add(R.drawable.image6);
        imageList5.add(R.drawable.image7);
        imageList5.add(R.drawable.image8);
        imageList5.add(R.drawable.image9);

        imageList6.add(R.drawable.image10);
        imageList6.add(R.drawable.image11);

        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(new Comment("小陈: ", "海南海口宇哥NB，66666666666666666666666666666666666666666666666666666666666666666666666666，我的宝贝"));
        comments.add(new Comment("小伍: ", "煜宝NB"));
        comments.add(new Comment("小杨: ", "钊钊NB"));

        discovers.add(new Discover(getString(R.string.nickname1), R.drawable.avatar1,
                getString(R.string.paragraph1), "1小时前", imageList1, comments));
        discovers.add(new Discover(getString(R.string.nickname2), R.drawable.avatar2,
                getString(R.string.paragraph2), "2小时前", imageList2, comments));
        discovers.add(new Discover(getString(R.string.nickname3), R.drawable.avatar3,
                getString(R.string.paragraph3), "3小时前", imageList3, null));
        discovers.add(new Discover(getString(R.string.nickname4), R.drawable.avatar4,
                getString(R.string.paragraph4), "4小时前", imageList4, null));
        discovers.add(new Discover(getString(R.string.nickname5), R.drawable.avatar5,
                getString(R.string.paragraph5), "5小时前", imageList5, null));
        discovers.add(new Discover(getString(R.string.nickname6), R.drawable.avatar6,
                getString(R.string.paragraph6), "6小时前", imageList6, null));

        // 设置LayoutManager及Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new DiscoverAdapter(discovers));

        // 设置朋友圈分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // commentEditText监听
        EditText commentEditText = view.findViewById(R.id.editCommentText);
        commentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                view.findViewById(R.id.sendCommentButton).setEnabled(!TextUtils.isEmpty(commentEditText.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 新朋友圈
        FloatingActionButton newDiscoverButton = view.findViewById(R.id.newDiscoverButton);
        newDiscoverButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NewDiscoverActivity.class);
            startActivity(intent);
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
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    private void hideActionBar(@NonNull View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        ((AppCompatActivity) context).getSupportActionBar().hide();
    }
}