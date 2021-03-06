package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.BooleanResponse
import com.example.mywechat.api.DiscoverResponse
import com.example.mywechat.repository.ChatRepository
import com.example.mywechat.repository.DiscoverRepository
import com.example.mywechat.repository.UserInfoRepository
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
class DiscoverViewModel @Inject constructor(
        private val discoverRepository: DiscoverRepository,
) : ViewModel() {
    val liveData = MutableLiveData<BooleanResponse?>(null)
    val discoverData = MutableLiveData<DiscoverResponse?>(null)

    fun discoverPost(msgType: String, text: String, file: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = discoverRepository.discoverPost(msgType, text, file)
                liveData.postValue(response)
            } catch (ignored : IOException) {}
        }
    }

    fun discoverMulPost(msgType: String, text: String, files: List<File>?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = discoverRepository.discoverMulPost(msgType, text, files)
                liveData.postValue(response)
            } catch (ignored : IOException) {}
        }
    }

    fun discover(lastUpdateTime: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = discoverRepository.discover(lastUpdateTime)
                discoverData.postValue(response)
            } catch (ignored : IOException) {}
        }
    }

    fun thumb(discoverId: String, thumb: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = discoverRepository.thumb(discoverId, thumb)
                liveData.postValue(response)
            } catch (ignored : IOException) {}
        }
    }

    fun comment(discoverId: String, sendTo: String?, msg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = discoverRepository.comment(discoverId, sendTo, msg)
                liveData.postValue(response)
            } catch (ignored : IOException) {}
        }
    }
}
