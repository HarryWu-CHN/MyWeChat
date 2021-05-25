package com.example.mywechat.repository

import android.util.Log
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.*
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
            myWeChatService.observeEvents().consumeEach {
                Log.d("webSocket event", "$it")
            }
        }
    }

    suspend fun login(username: String, password: String, onResponse: (Boolean) -> Unit) {
        return suspendCoroutine { continuation ->
            GlobalScope.launch(CoroutineExceptionHandler { _, throwable ->
            }) {
                val channel = myWeChatService.observeLoginResponse()
                channel.consumeEach {
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