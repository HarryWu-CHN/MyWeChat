package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.UserGetResponse
import com.example.mywechat.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel

// liveData params got what the response is, you can get it at fragment/activity, according to RegisterFragment : liveData.observe()
class UserInfoViewModel @Inject constructor(
        private val userInfoRepository: UserInfoRepository,
) : ViewModel() {
    val liveData = MutableLiveData<UserGetResponse?>(null)
    fun userGet(userToGet : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userInfoRepository.userGet(userToGet)
                liveData.postValue(response)
            } catch (ignored : IOException){

            }
        }
    }
}