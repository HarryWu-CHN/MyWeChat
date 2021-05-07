package com.example.mywechat.ui.dialog;

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

import com.example.mywechat.R;

public class DialogFragment extends Fragment {

    private DialogViewModel dialogViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dialogViewModel =
                new ViewModelProvider(this).get(DialogViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dialog, container, false);
        final TextView textView = root.findViewById(R.id.text_dialog);
        dialogViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}