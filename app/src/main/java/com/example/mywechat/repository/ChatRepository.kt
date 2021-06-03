package com.example.mywechat.repository

import com.example.mywechat.api.ApiService
import com.example.mywechat.api.ChatSendRequest
import java.io.File
import javax.inject.Inject

class ChatRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun chatSend(
            sendTo: String,
            msgType: String,
            msg: String,
            file: File?
    ) = apiService.chatSend(
            ChatSendRequest(
                    sendTo, msgType, msg, file
            )
    )
}