package com.example.mywechat.repository

import com.example.mywechat.api.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class DiscoverRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun discoverPost(
            msgType: String,
            text: String,
            files: List<File>?,
    ): BooleanResponse {
        files?.let {
            val parts: MutableList<MultipartBody.Part> = ArrayList(files.size)
            for (file in files) {
                val requestBody: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val part: MultipartBody.Part = MultipartBody.Part.createFormData("files", file.name, requestBody)
                (parts as ArrayList<MultipartBody.Part>).add(part)
            }
            return apiService.discoverPost(
                msgType.toRequestBody("text/plain".toMediaTypeOrNull()),
                text.toRequestBody("text/plain".toMediaTypeOrNull()),
                parts
            )
        }
        return apiService.discoverPost(
            msgType.toRequestBody("text/plain".toMediaTypeOrNull()),
            text.toRequestBody("text/plain".toMediaTypeOrNull()),
            ArrayList(0)
        )
    }

    suspend fun discover(
            lastUpdateTime : Long
    ) = apiService.discover(
            DiscoverRequest(
                    lastUpdateTime
            )
    )

    suspend fun thumb(
            discoverId : String,
            thumb : String,
    ) = apiService.thumb(
            ThumbRequest(
                    discoverId,
                    thumb
            )
    )

    suspend fun comment(
            discoverId: String,
            sendTo: String,
            msg: String,
    ) = apiService.comment(
            CommentRequest(
                    discoverId,
                    sendTo,
                    msg,
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