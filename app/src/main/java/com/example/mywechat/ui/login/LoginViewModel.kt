package com.example.mywechat.ui.login

import androidx.lifecycle.ViewModel
import com.example.mywechat.repository.WSRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(val repository: WSRepository) : ViewModel() {
    suspend fun login(username: String, password: String) {
        repository.login(username, password, onResponse = {

        })
    }
}