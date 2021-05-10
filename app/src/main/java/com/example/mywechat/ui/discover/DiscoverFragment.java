package com.example.mywechat.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.R;

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

        discovers.add(new Discover(getString(R.string.nickname1), R.drawable.avatar1,
                getString(R.string.paragraph1), "1小时前", imageList1));
        discovers.add(new Discover(getString(R.string.nickname2), R.drawable.avatar2,
                getString(R.string.paragraph2), "2小时前", imageList2));
        discovers.add(new Discover(getString(R.string.nickname3), R.drawable.avatar3,
                getString(R.string.paragraph3), "3小时前", imageList3));
        discovers.add(new Discover(getString(R.string.nickname4), R.drawable.avatar4,
                getString(R.string.paragraph4), "4小时前", imageList4));
        discovers.add(new Discover(getString(R.string.nickname5), R.drawable.avatar5,
                getString(R.string.paragraph5), "5小时前", imageList5));
        discovers.add(new Discover(getString(R.string.nickname6), R.drawable.avatar6,
                getString(R.string.paragraph6), "6小时前", imageList6));

        // 设置LayoutManager及Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new DiscoverAdapter(discovers));
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

//    private DiscoverViewModel discoverViewModel;
//
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        discoverViewModel =
//                new ViewModelProvider(this).get(DiscoverViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_discover, container, false);
//        final TextView textView = root.findViewById(R.id.text_discover);
//        discoverViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
}