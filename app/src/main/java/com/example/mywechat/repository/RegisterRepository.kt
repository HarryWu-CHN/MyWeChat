package com.example.mywechat.repository

import com.example.mywechat.api.ApiService
import com.example.mywechat.api.SignUpRequest
import javax.inject.Inject

class RegisterRepository @Inject constructor(
        private val apiService : ApiService,
) {
    suspend fun register (
        username : String,
        password : String,
    ) = apiService.register(
            SignUpRequest(
                    username = username,
                    password = password,
            )
    )
}
