package com.example.mywechat.repository

import com.example.mywechat.api.ApiService
import com.example.mywechat.api.ChatDeleteRequest
import com.example.mywechat.api.ChatRecordGetRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ChatRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun chatSend(
            sendTo: String,
            msgType: String,
            msg: String?,
            file: File?
    ) = apiService.chatSend(
            sendTo.toRequestBody("text/plain".toMediaTypeOrNull()),
            msg?.toRequestBody("text/plain".toMediaTypeOrNull()),
            msgType.toRequestBody("text/plain".toMediaTypeOrNull()),
            file?.let {
                MultipartBody.Part.createFormData("file", it.name, it.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
            }
    )
    suspend fun chatRecordGet(
            sendTo: String
    ) = apiService.chatRecordGet(
            ChatRecordGetRequest(sendTo)
    )
    suspend fun chatDelete(
            sendTo: String
    ) = apiService.chatDelete(ChatDeleteRequest(sendTo))
    suspend fun contactDelete(
            sendTo: String
    ) = apiService.contactDelete(ChatRecordGetRequest(sendTo))
}