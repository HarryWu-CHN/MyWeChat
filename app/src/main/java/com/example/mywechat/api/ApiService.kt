package com.example.mywechat.api

import com.squareup.moshi.JsonClass
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("user/logon")
    suspend fun register(@Body request: SignUpRequest) : SignUpResponse



}

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val username : String,
    val password : String,
)

@JsonClass(generateAdapter = true)
data class SignUpResponse (
    val success : Boolean,
)
