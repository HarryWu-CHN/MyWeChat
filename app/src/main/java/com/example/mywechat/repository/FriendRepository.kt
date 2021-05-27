package com.example.mywechat.repository

import com.example.mywechat.api.ApiService
import com.example.mywechat.api.ContactAddRequest
import com.example.mywechat.api.ContactAgreeRequest
import com.example.mywechat.api.ContactFindRequest
import javax.inject.Inject

class FriendRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun contactFind (
            find : String,
    ) = apiService.contactFind(
            ContactFindRequest(
                    find = find
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


}