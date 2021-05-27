package com.example.mywechat.repository

import com.example.mywechat.api.*
import javax.inject.Inject

class DiscoverRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun discoverPost(
            msgType: String,
            msg : String,
    ) = apiService.discoverPost(
            DiscoverPostRequest(
                    msgType, msg,
            )
    )

    suspend fun discover(
            lastUpdateTime : Long
    ) = apiService.discover(
            DiscoverRequest(
                    lastUpdateTime
            )
    )

    suspend fun discoverUser(
            discoverUser : String,
            timePeriod : ArrayList<TimePair>
    ) = apiService.discoverUser(
            DiscoverUserRequest(
                    discoverUser, timePeriod
            )
    )
}