package com.example.mywechat.ui.contacts;

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

import java.util.LinkedList;

public class ContactFragment extends Fragment {

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
        recyclerView = view.findViewById(R.id.contacts_recylerview);

        // 添加数据，为recyclerView绑定Adapter、LayoutManager
        // 添加数据的样例代码如下:

        LinkedList<Contact> contacts = new LinkedList<>();
        contacts.add(new Contact(getString(R.string.nickname1), R.drawable.avatar1));
        contacts.add(new Contact(getString(R.string.nickname2), R.drawable.avatar2));
        contacts.add(new Contact(getString(R.string.nickname3), R.drawable.avatar3));
        contacts.add(new Contact(getString(R.string.nickname4), R.drawable.avatar4));
        contacts.add(new Contact(getString(R.string.nickname5), R.drawable.avatar5));
        contacts.add(new Contact(getString(R.string.nickname6), R.drawable.avatar6));
        contacts.add(new Contact(getString(R.string.nickname7), R.drawable.avatar7));
        contacts.add(new Contact(getString(R.string.nickname8), R.drawable.avatar8));
        contacts.add(new Contact(getString(R.string.nickname9), R.drawable.avatar9));
        contacts.add(new Contact(getString(R.string.nickname10), R.drawable.avatar10));
        contacts.add(new Contact(getString(R.string.nickname11), R.drawable.avatar11));
        contacts.add(new Contact(getString(R.string.nickname12), R.drawable.avatar12));

        // 设置LayoutManager及Adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ContactAdapter(contacts));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

//    private ContactViewModel contactViewModel;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        contactViewModel =
//                new ViewModelProvider(this).get(ContactViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_contacts, container, false);
//        final TextView textView = root.findViewById(R.id.text_contacts);
//        contactViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
//    }
}