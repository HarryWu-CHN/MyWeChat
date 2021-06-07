package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.ChatSendResponse
import com.example.mywechat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
// liveData params got what the response is, you can get it at fragment/activity, according to RegisterFragment : liveData.observe()
class ChatSendViewModel @Inject constructor(
        private val chatRepository: ChatRepository,
) : ViewModel() {
    val liveData = MutableLiveData<ChatSendResponse?>(null)
    fun chatSend(sendTo : String, msgType : String, msg : String?, file : File?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = chatRepository.chatSend(sendTo, msgType, msg, file)
                val chatSendResponse = ChatSendResponse (
                        response.component1(),
                        response.component2(),
                        msgType,
                        msg,
                        file
                )
                liveData.postValue(chatSendResponse)
            } catch (ignored : IOException){
                liveData.postValue(null)
            }
        }
    }
}
