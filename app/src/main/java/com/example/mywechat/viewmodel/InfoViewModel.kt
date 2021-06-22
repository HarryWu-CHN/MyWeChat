package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.UserGetResponse
import com.example.mywechat.repository.PasswordEditRepository
import com.example.mywechat.repository.UserEditRepository
import com.example.mywechat.repository.UserInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
@FlowPreview
@ExperimentalCoroutinesApi
class InfoViewModel @Inject constructor(
        private val userEditRepository: UserEditRepository,
        private val userInfoRepository: UserInfoRepository,
        private val passwordEditRepository: PasswordEditRepository,
) : ViewModel() {
    private val userEditResult = MutableLiveData<Boolean?>(null)
    private val userInfoLiveData = MutableLiveData<UserGetResponse?>(null)
    val passwordEditResult = MutableLiveData<Boolean?>(null)

    private suspend fun passwordEdit(old_p : String, new_p:String) {
        passwordEditResult.postValue(null)
        try {
            val response = passwordEditRepository.passwordEdit(old_p, new_p)
            passwordEditResult.postValue(response.success)
        }catch (e: IOException){
            passwordEditResult.postValue(false)
        }
    }

    fun callPasswordEdit(old_p : String, new_p:String) {
        viewModelScope.launch (Dispatchers.IO) {
            passwordEdit(old_p,new_p)
        }
    }
    fun userGet(userToGet : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userInfoRepository.userGet(userToGet)
                userInfoLiveData.postValue(response)
            } catch (ignored : IOException){

            }
        }
    }

    private suspend fun userEdit(nickname : String?, icon: File?) {
        userEditResult.postValue(null)
        try {
            val response = userEditRepository.userEdit(nickname, icon)
            userEditResult.postValue(response.success)
            // Log.d("")
        }catch (e: IOException){
            userEditResult.postValue(false)
        }
    }

    fun callUserEdit(nickname: String?, icon: File?) {
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