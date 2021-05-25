package com.example.mywechat.repository

import android.graphics.Bitmap
import android.provider.ContactsContract
import com.example.mywechat.api.ApiService
import com.example.mywechat.api.UserEditRequest
import javax.inject.Inject

class UserEditRepository @Inject constructor(
        private val apiService: ApiService,
) {
    suspend fun userEdit (
            nickname : String?,
            icon : Bitmap?,
    ) = apiService.userEdit(
            UserEditRequest(
                    nickname = nickname,
                    icon = icon,
            )
    )
}