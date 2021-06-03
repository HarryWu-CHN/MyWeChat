package com.example.mywechat.repository

import com.example.mywechat.api.*
import javax.inject.Inject

class FriendRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun contactFind (
            userToFind : String,
    ) = apiService.contactFind(
            ContactFindRequest(
                    userToFind = userToFind
            )
    )

    suspend fun contactAdd (
            sendTo : String,
    ) = apiService.contactAdd(
            ContactAddRequest(
                    sendTo = sendTo
            )
    )

    suspend fun contactAgree (
            sendTo : String,
            agree : Boolean
    ) = apiService.contactAgree(
            ContactAgreeRequest(
                    sendTo = sendTo,
                    agree = agree,
            )
    )

    suspend fun contactWaited (

    ) = apiService.contactWaited(
            ContactWaitedText(
                    username = "yang",
            )
    )
}