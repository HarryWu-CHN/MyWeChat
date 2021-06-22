package com.example.mywechat.repository

import android.util.Log
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class WSRepository @Inject constructor(
        private val myWeChatService: MyWeChatService,
) {
    init {
        GlobalScope.launch(Dispatchers.IO) {
            myWeChatService.observeEvents().consumeEach {0
                Log.d("webSocket event", it.toString())
            }
        }
    }

    fun newMessage() : ReceiveChannel<Array<NewMessage>> {
        val it = myWeChatService.observeNewMessage()
        Log.d("Repository", "New message$it")
        return it

    }

    fun newFriendApply() : ReceiveChannel<Array<NewFriendApply>> {
        val it = myWeChatService.observeNewFriendApply()
        Log.d("Repository", "New Friend Apply$it")
        return it
    }

    fun friendApplyResponse() : ReceiveChannel<FriendApplyResponse>{
        return myWeChatService.observeFriendApplyResponse()
    }

    fun newGroupInvite() : ReceiveChannel<NewGroupInvite>{
        return myWeChatService.observeNewGroupInvite()
    }

    fun getKickOut() : ReceiveChannel<GetKickOut>{
        return myWeChatService.observeGetKickOut()
    }

    fun newDiscover() : ReceiveChannel<NewDiscover>{
        return myWeChatService.observeNewDiscover()
    }

    suspend fun login(username: String, password: String, onResponse: (Boolean) -> Unit) {
        return suspendCoroutine { continuation ->
            GlobalScope.launch(CoroutineExceptionHandler { _, throwable ->
            }) {
                val channel = myWeChatService.observeLoginResponse()
                channel.consumeEach {
                    Log.d("test1", it.toString())
                    println("test1.1")
                    println(it.toString())
                    continuation.resume(onResponse(it.success))
                    channel.cancel()
                }
            }
            if (!myWeChatService.login(LoginRequest(username, password))) {
                onResponse(false)
            }
        }
    }
}