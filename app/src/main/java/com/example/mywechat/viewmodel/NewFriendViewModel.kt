package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.ContactFindResponse
import com.example.mywechat.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@FlowPreview
@ExperimentalCoroutinesApi
// liveData params got what the response is, you can get it at fragment/activity, according to RegisterFragment : liveData.observe()
class NewFriendViewModel @Inject constructor(private val friendRepository: FriendRepository) : ViewModel() {
    val liveData = MutableLiveData<ContactFindResponse?>(null)
    fun contactFind(friendToFind : String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val response = friendRepository.contactFind(friendToFind)
                liveData.postValue(response)
            } catch (e: IOException) {
            }
        }
    }
    fun contactAdd(sendTo: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                friendRepository.contactAdd(sendTo)
            } catch (e: IOException) {}
        }
    }
    fun contactAgree(sendTo: String, agree: Boolean) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                friendRepository.contactAgree(sendTo, agree)
            } catch (e: IOException) {}
        }
    }
}
