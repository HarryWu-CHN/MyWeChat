package com.example.mywechat.repository

import com.example.mywechat.api.*
import io.reactivex.internal.util.ArrayListSupplier
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class GroupRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun groupCreate(
            groupName : String?,
            membersName : List<String>,
    ) = apiService.groupCreate(
            GroupCreateRequest(
            groupName, membersName
    ))

    suspend fun groupSend(
            groupId: String,
            msg: String?,
            msgType: String,
            file: File?
    ) = apiService.groupSend(
            groupId.toRequestBody("text/plain".toMediaTypeOrNull()),
            msg?.toRequestBody("text/plain".toMediaTypeOrNull()),
            msgType.toRequestBody("text/plain".toMediaTypeOrNull()),
            file?.let {
                MultipartBody.Part.createFormData("file", it.name, it.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
            }
    )
    suspend fun groupRecord(
            groupId: String
    ) = apiService.groupRecord(GroupRecordRequest(groupId))

    suspend fun groupEdit(
            groupId : String?,
            groupName: String?,
            groupType: String?,
    ) = apiService.groupEdit(
            GroupEditRequest(
                    groupId, groupName, groupType
            )
    )

    suspend fun groupHandover(
            groupId: String,
            handTo : String,
    ) = apiService.groupHandover(
            GroupHandoverRequest(
                    groupId, handTo
            )
    )

    suspend fun groupAdd(
            groupId: String,
            userAdd: String,
    ) = apiService.groupAdd(
            GroupAddRequest(
                    groupId, userAdd
            )
    )

    suspend fun groupKick(
            groupId: String,
            userKick: String,
    ) = apiService.groupKick(
            GroupKickRequest(
                    groupId, userKick
            )
    )

    suspend fun groupExit(
            groupId: String,
    ) = apiService.groupExit(
            GroupExitRequest(
                    groupId
            )
    )

    suspend fun groupDel(
            groupId: String,
    ) = apiService.groupDel(
            GroupDelRequest(
                    groupId
            )
    )
}