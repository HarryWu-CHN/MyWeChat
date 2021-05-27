package com.example.mywechat.ui.login

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mywechat.MainActivity
import com.example.mywechat.UserActivity
import com.example.mywechat.repository.WSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: WSRepository) : ViewModel() {
    suspend fun login(username: String, password: String, onResponse: (Boolean) -> Unit) {
        repository.login(username, password, onResponse)
    }
}