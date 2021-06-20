package com.example.mywechat.repository

import com.example.mywechat.api.*
import java.io.File
import javax.inject.Inject

class DiscoverRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun discoverPost(
            msgType: String,
            text: String,
            files: List<File>,
    ) = apiService.discoverPost(
            DiscoverPostRequest(
                    msgType, text, files
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