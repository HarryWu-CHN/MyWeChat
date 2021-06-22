package com.example.mywechat.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mywechat.App
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.reactive.collect
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class WSRepository @Inject constructor(
        private val myWeChatService: MyWeChatService,
) {
//    private val usernameLivedata = MutableLiveData<String?>(null)
//    private val passwordLivedata = MutableLiveData<String?>(null)
    init {
        GlobalScope.launch(Dispatchers.IO) {
            myWeChatService.observeEvents().consumeEach {
                Log.d("webSocket event", it.toString())
            }
//            observeOnConnectionOpenedEvent().collect {
//                myWeChatService.login(LoginRequest(
//                        username = usernameLivedata.value.toString(),
//                        password = passwordLivedata.value.toString(),
//                ))
//            }
        }

    }

    fun observeOnConnectionOpenedEvent(): ReceiveChannel<WebSocket.Event> {
        return myWeChatService.observeEvents()
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
//                usernameLivedata.postValue(username)
//                passwordLivedata.postValue(password)
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