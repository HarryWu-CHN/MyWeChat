package com.example.mywechat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.repository.NewFriendApply
import com.example.mywechat.repository.WSRepository
import com.example.mywechat.ui.contacts.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@FlowPreview
@ExperimentalCoroutinesApi
class MainActivityViewModel @Inject constructor(
        private val wsRepository: WSRepository
) : ViewModel() {
    val newFriendApply = MutableLiveData<Array<NewFriendApply>?>(null)
    fun observeNewFriendApply() {
        viewModelScope.launch(Dispatchers.IO) {
            wsRepository.newFriendApply().consumeEach {
                Log.d("ViewModel", "New Friend Apply$it")
                newFriendApply.postValue(it)
            }
        }
    }
}