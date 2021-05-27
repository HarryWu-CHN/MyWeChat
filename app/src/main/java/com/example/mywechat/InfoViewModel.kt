package com.example.mywechat

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.repository.UserEditRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Runnable
import javax.inject.Inject

@HiltViewModel
@FlowPreview
@ExperimentalCoroutinesApi
class InfoViewModel @Inject constructor(
        private val userEditRepository: UserEditRepository,
) : ViewModel() {
    private val liveData = MutableLiveData<Boolean?>(null)
    private suspend fun userEdit(nickname : String?, icon: Bitmap?) {
        liveData.postValue(null)
        try {
            val response = userEditRepository.userEdit(nickname, icon)
            liveData.postValue(response.success)
            // Log.d("")
        }catch (e: IOException){
            liveData.postValue(false)
        }
    }

    fun callUserEdit(nickname: String?, icon: Bitmap?) {
        viewModelScope.launch (Dispatchers.IO) {
            userEdit(nickname, icon)
        }

//    fun callUserEdit(callback: Runnable) {
//        viewModelScope.launch(Dispatchers.IO) {
//            userEdit()
//            callback.run()
//        }
    }
}