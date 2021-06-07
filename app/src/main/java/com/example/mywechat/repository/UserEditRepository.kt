package com.example.mywechat.repository

import android.graphics.Bitmap
import android.provider.ContactsContract
import com.example.mywechat.api.ApiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserEditRepository @Inject constructor(
        private val apiService: ApiService,
) {
    suspend fun userEdit (
            nickname : String?,
            icon : File?,
    ) = apiService.userEdit(
            // 是否为空的处理
            nickname?.toRequestBody("text/plain".toMediaTypeOrNull()),
            icon?.let {
                MultipartBody.Part.createFormData("icon", it.name, RequestBody.create("multipart/form-data".toMediaTypeOrNull(), it))
            }
        )
}