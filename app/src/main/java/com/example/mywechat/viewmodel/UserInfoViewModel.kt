package com.example.mywechat.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.App
import com.example.mywechat.api.UserGetResponse
import com.example.mywechat.repository.UserInfoRepository
import com.example.mywechat.repository.WSRepository
import com.tinder.scarlet.WebSocket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.collect
import kotlinx.coroutines.reactive.consumeEach
import java.io.IOException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel

// liveData params got what the response is, you can get it at fragment/activity, according to RegisterFragment : liveData.observe()
class UserInfoViewModel @Inject constructor(
        private val userInfoRepository: UserInfoRepository,
        private val wsRepository: WSRepository,
) : ViewModel() {
    fun openReLogin(userName: String, password: String) {
        viewModelScope.launch {
            wsRepository.observeOnConnectionOpenedEvent().consumeEach {
                Log.d("reconnect...........", userName + password + it.toString())
                if (userName != "" && password != "") {
                    wsRepository.login(userName, password) {}
                }
            }
        }
    }

    val liveData = MutableLiveData<UserGetResponse?>(null)
    fun userGet(userToGet : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userInfoRepository.userGet(userToGet)
                liveData.postValue(response)
            } catch (ignored : IOException){
                ignored.printStackTrace()
            }
        }
    }
}