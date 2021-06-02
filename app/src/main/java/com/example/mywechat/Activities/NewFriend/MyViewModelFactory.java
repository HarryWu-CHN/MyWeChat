/**
package com.example.mywechat.Activities.NewFriend;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mywechat.api.ApiService;
import com.example.mywechat.module.AppModule;
import com.example.mywechat.repository.FriendRepository;
import com.example.mywechat.viewmodel.NewFriendViewModel;

import org.jetbrains.annotations.NotNull;

public class MyViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    public MyViewModelFactory(Application application) {
        mApplication = application;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
        // Replace UserViewModel →  with whatever or however you create your ViewModel
        AppModule apiModule = new AppModule();
        FriendRepository friendRepository = new FriendRepository(apiModule);
        return (T) new NewFriendViewModel(friendRepository);
    }
}*/