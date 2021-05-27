package com.example.mywechat.repository

import com.example.mywechat.api.*
import io.reactivex.internal.util.ArrayListSupplier
import javax.inject.Inject

class GroupRepository @Inject constructor(
        private val apiService: ApiService
) {
    suspend fun groupCreate(
            groupName : String,
            membersName : ArrayList<String>,
    ) = apiService.groupCreate(
            GroupCreateRequest(
            groupName, membersName
    ))

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