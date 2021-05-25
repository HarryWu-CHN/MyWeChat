package com.example.mywechat.repository

import com.squareup.moshi.JsonClass
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

interface MyWeChatService {
    @Send
    fun login(request: LoginRequest): Boolean

    @Receive
    fun observeEvents(): ReceiveChannel<WebSocket.Event>

    @Receive
    fun observeLoginResponse(): ReceiveChannel<LoginSuccessResponse>
}

@JsonClass(generateAdapter = true)
data class LoginSuccessResponse(
        val success: Boolean
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
        val username: String,
        val password: String,
        val bizType: String = "USER_LOGIN",
)
