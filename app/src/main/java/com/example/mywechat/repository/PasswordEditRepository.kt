package com.example.mywechat.repository

import com.example.mywechat.api.ApiService
import com.example.mywechat.api.PasswordEditRequest
import javax.inject.Inject

class PasswordEditRepository @Inject constructor(
        private val apiService: ApiService,
) {
    suspend fun passwordEdit (
            old_password: String,
            new_password: String,
    ) = apiService.passwordEdit(
            PasswordEditRequest(
                    old_password, new_password
            )
    )
}
