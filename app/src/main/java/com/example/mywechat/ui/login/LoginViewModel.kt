package com.example.mywechat.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.repository.RegisterRepository
import com.example.mywechat.repository.WSRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(val repository: WSRepository,
                                         val registerRepository : RegisterRepository) : ViewModel() {
    val successfulLogin = MutableLiveData<LoginStatus>(LoginStatus(wsLogin = false, httpLogin = false))

    suspend fun login(username: String, password: String, onResponse: (Boolean) -> Unit) {
        repository.login(username, password, onResponse)
    }
    suspend fun httpLogin(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = registerRepository.httpLogin(username, password)
                val wsLogin = successfulLogin.value!!.wsLogin
                successfulLogin.postValue(LoginStatus(wsLogin, response.success))

            } catch (e : IOException) { }
        }
    }
}

data class LoginStatus(
        var wsLogin: Boolean,
        var httpLogin: Boolean,
)