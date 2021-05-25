package com.example.mywechat.api

import android.graphics.Bitmap
import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/logon")
    suspend fun register(@Body request: SignUpRequest) : BooleanResponse

    @POST("user/edit")
    suspend fun userEdit(@Body request: UserEditRequest) : BooleanResponse



}

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val username : String,
    val password : String,
)

@JsonClass(generateAdapter = true)
data class BooleanResponse (
    val success : Boolean,
)


@JsonClass(generateAdapter = true)
data class UserEditRequest(
        var nickname : String?,
        var icon : Bitmap?,
)
