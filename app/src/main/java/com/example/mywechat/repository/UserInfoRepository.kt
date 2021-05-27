package com.example.mywechat.repository

import com.example.mywechat.api.ApiService
import com.example.mywechat.api.UserGetRequest
import javax.inject.Inject

class UserInfoRepository @Inject constructor(
        private val  apiService: ApiService
){
    suspend fun userGet (
            userToGet : String,
    ) = apiService.userGet(
            UserGetRequest(
                    userToGet = userToGet
            )
    )
}

