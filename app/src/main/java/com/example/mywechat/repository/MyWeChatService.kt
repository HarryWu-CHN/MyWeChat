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

    @Receive
    fun observeNewMessage(): ReceiveChannel<Array<NewMessage>>

    @Receive
    fun observeNewFriendApply(): ReceiveChannel<Array<NewFriendApply>>

    @Receive
    fun observeFriendApplyResponse(): ReceiveChannel<FriendApplyResponse>

    @Receive
    fun observeNewGroupInvite(): ReceiveChannel<NewGroupInvite>

    @Receive
    fun observeGetKickOut(): ReceiveChannel<GetKickOut>

    @Receive
    fun observeNewDiscover(): ReceiveChannel<NewDiscover>

}

@JsonClass(generateAdapter = true)
data class NewFriendApply(
        val infoType: Int,
        val from: String,
        val success: Boolean,
)

@JsonClass(generateAdapter = true)
data class FriendApplyResponse(
        val infoType: Int,
        val friendName: String,
        val agree: Boolean,
)

@JsonClass(generateAdapter = true)
data class NewGroupInvite(
        val infoType: Int,
        val inviter: String,
)

@JsonClass(generateAdapter = true)
data class GetKickOut(
        val infoType: Int,
        val groupName: String,
)

@JsonClass(generateAdapter = true)
data class NewDiscover(
        val infoType: Int,
        val friendName: String,
)

@JsonClass(generateAdapter = true)
data class NewMessage(
        val from: String,
        val infoType: Int,
        val msg: String,
        val msgType: String,
)


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
