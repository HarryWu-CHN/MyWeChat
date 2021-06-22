package com.example.mywechat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mywechat.api.BooleanResponse
import com.example.mywechat.api.ChatRecordGetResponse
import com.example.mywechat.api.ChatSendResponse
import com.example.mywechat.repository.GroupRepository
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
class GroupViewModel @Inject constructor(
        private val groupRepository: GroupRepository
) : ViewModel() {
    val createResLiveData = MutableLiveData<BooleanResponse?>(null)
    fun groupCreate(groupName : String?, membersName : List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = groupRepository.groupCreate(groupName, membersName)
                createResLiveData.postValue(response)
            } catch (ignored : IOException){
                ignored.printStackTrace()
            }
        }
    }
    val groupSendLiveData = MutableLiveData<ChatSendResponse?>(null)
    fun groupSend(groupId: String, msg: String?, msgType: String, file: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = groupRepository.groupSend(groupId, msg, msgType, file)
                val chatSendResponse = ChatSendResponse (
                        response.component1(),
                        response.component2(),
                        msgType,
                        msg,
                        file
                )
                groupSendLiveData.postValue(chatSendResponse)
            } catch (ignored : IOException){
                ignored.printStackTrace();
            }
        }
    }
    val groupRecordLiveData = MutableLiveData<ChatRecordGetResponse?>(null)
    fun groupRecord(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = groupRepository.groupRecord(groupId)
                groupRecordLiveData.postValue(response)
            } catch (ignored : IOException){
                ignored.printStackTrace()
            }
        }
    }
    val exitResLiveData = MutableLiveData<BooleanResponse?>(null)
    fun groupExit(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = groupRepository.groupExit(groupId)
                exitResLiveData.postValue(response)
            } catch (ignored : IOException){
                ignored.printStackTrace()
            }
        }
    }
    val addResLiveData = MutableLiveData<BooleanResponse?>(null)
    fun groupAdd(groupId: String, userAdd: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = groupRepository.groupAdd(groupId, userAdd)
                addResLiveData.postValue(response)
            } catch (ignored : IOException){
                ignored.printStackTrace()
            }
        }
    }
    val kickResLiveData = MutableLiveData<BooleanResponse?>(null)
    fun groupKick(groupId: String, userKick: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = groupRepository.groupKick(groupId, userKick)
                kickResLiveData.postValue(response)
            } catch (ignored : IOException){
                ignored.printStackTrace()
            }
        }
    }
}
