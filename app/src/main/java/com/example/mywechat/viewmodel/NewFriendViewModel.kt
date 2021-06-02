package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.ContactFindResponse
import com.example.mywechat.api.UserGetResponse
import com.example.mywechat.repository.FriendRepository
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
class NewFriendViewModel @Inject constructor(
        private val friendRepository: FriendRepository,
) : ViewModel() {
    val liveData = MutableLiveData<ContactFindResponse?>(null)
    fun contactFind(friendToFind : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = friendRepository.contactFind(friendToFind)
                liveData.postValue(response)
            } catch (ignored : IOException){

            }
        }
    }
}