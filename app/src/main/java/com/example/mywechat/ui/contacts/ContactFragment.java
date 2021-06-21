package com.example.mywechat.ui.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywechat.Activities.NewFriend.FriendApplyActivity;
import com.example.mywechat.App;
import com.example.mywechat.R;
import com.example.mywechat.model.FriendRecord;

import org.litepal.LitePal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class ContactFragment extends Fragment {

    private Button friendApplyButton;
    private RecyclerView recyclerView;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance() {
        return new ContactFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showActionBar(view);

        friendApplyButton = view.findViewById(R.id.friendApplyButton);
        friendApplyButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FriendApplyActivity.class);
            startActivity(intent);
        });
        App app = (App) getActivity().getApplication();
        List<FriendRecord> tmpList = LitePal.where("userName = ?", app.getUsername()).find(FriendRecord.class);
        FriendRecord friendRecord = null;
        if (tmpList != null && tmpList.size() > 0)
            friendRecord = tmpList.get(0);
        // 添加数据，为recyclerView绑定Adapter, LayoutManager
        // 添加数据的样例代码如下:
        if (friendRecord != null) {
            LinkedList<Contact> contacts = new LinkedList<>();
            List<String> friendsName = friendRecord.getFriendsName();
            List<String> friendsIcon = friendRecord.getFriendsIcon();
            for (int i = 0; i < friendsName.size(); i++) {
                try {
                    FileInputStream fis = new FileInputStream(friendsIcon.get(i));
                    Bitmap bitmap = BitmapFactory.decodeStream(fis);
                    contacts.add(new Contact(friendsName.get(i), bitmap));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            // 设置LayoutManager及Adapter
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView = view.findViewById(R.id.contacts_recyclerview);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new ContactAdapter(contacts));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    private void showActionBar(@NonNull View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        ((AppCompatActivity) context).getSupportActionBar().show();
    }

    /** TODO 保存bitmap到外部存储的方法法
    public static File saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     */
}