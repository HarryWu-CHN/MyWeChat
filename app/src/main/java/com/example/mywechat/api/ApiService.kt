package com.example.mywechat.api

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.BoolRes
import com.squareup.moshi.JsonClass
import dagger.hilt.internal.GeneratedEntryPoint
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.sql.Time

interface ApiService {
    @POST("user/logon")
    suspend fun register(@Body request: SignUpRequest) : BooleanResponse

    @POST("user/edit")
    suspend fun userEdit(@Body request: UserEditRequest) : BooleanResponse

    @POST("user/get")
    suspend fun userGet(@Body request: UserGetRequest) : UserGetResponse

    // 好友相关
    @POST("contact/find")
    suspend fun contactFind(@Body request: ContactFindRequest) : ContactFindResponse

    @POST("contact/add")
    suspend fun contactAdd(@Body request: ContactAddRequest) : BooleanResponse

    @POST("contact/agree")
    suspend fun contactAgree(@Body request: ContactAgreeRequest) : BooleanResponse

    //群聊相关
    @POST("group/create")
    suspend fun groupCreate(@Body request: GroupCreateRequest) : BooleanResponse

    @POST("group/edit")
    suspend fun groupEdit(@Body request: GroupEditRequest) : BooleanResponse

    @POST("group/handover")
    suspend fun groupHandover(@Body request: GroupHandoverRequest) : BooleanResponse

    @POST("group/add")
    suspend fun groupAdd(@Body request: GroupAddRequest) : BooleanResponse

    @POST("group/kick")
    suspend fun groupKick(@Body request: GroupKickRequest) : BooleanResponse

    @POST("group/exit")
    suspend fun groupExit(@Body request: GroupExitRequest) : BooleanResponse

    @POST("group/del")
    suspend fun groupDel(@Body request : GroupDelRequest) : BooleanResponse

    //发消息，朋友圈相关
    @POST("chat/send")
    suspend fun chatSend(@Body request : ChatSendRequest) : BooleanResponse

    @POST("discover/post")
    suspend fun discoverPost(@Body request: DiscoverPostRequest) : BooleanResponse

    @POST("discover")
    suspend fun discover(@Body request: DiscoverRequest) : DiscoverResponse

    @POST("discover/user")
    suspend fun discoverUser(@Body request: DiscoverUserRequest) : DiscoverUserResponse


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
        val nickname : String?,
        val icon : Bitmap?,
)

@JsonClass(generateAdapter = true)
data class UserGetRequest(
        val userToGet: String
)

@JsonClass(generateAdapter = true)
data class UserGetResponse(
        val success : Boolean,
        val username : String,
        val icon : Bitmap,
        val joinTime : String,
)

@JsonClass(generateAdapter = true)
data class ContactFindRequest(
        val find : String,
)

@JsonClass(generateAdapter = true)
data class ContactFindResponse(
        val success : Boolean,
        val userNames : ArrayList<String>,
        val userIcons : ArrayList<String>,
)

@JsonClass(generateAdapter = true)
data class ContactAddRequest(
        val sendTo : String,
)

@JsonClass(generateAdapter = true)
data class ContactAgreeRequest(
        val sendTo : String,
        val agree : Boolean,
)

@JsonClass(generateAdapter = true)
data class GroupCreateRequest(
        val groupName : String,
        val membersName : ArrayList<String>,
)

@JsonClass(generateAdapter = true)
data class GroupEditRequest(
        val groupId : String?,
        val groupName : String?,
        val groupType : String?,
)

@JsonClass(generateAdapter = true)
data class GroupHandoverRequest(
        val groupId : String,
        val handTo : String,
)

@JsonClass(generateAdapter = true)
data class GroupAddRequest(
        val groupId : String,
        val userAdd : String,
)

@JsonClass(generateAdapter = true)
data class GroupKickRequest(
        val groupId : String,
        val userKick : String,
)

@JsonClass(generateAdapter = true)
data class GroupExitRequest(
        val groupId : String,
)

@JsonClass(generateAdapter = true)
data class GroupDelRequest(
        val groupId : String,
)

@JsonClass(generateAdapter = true)
data class ChatSendRequest(
        val sendTo: String,
        val msgTpye: String,
        val msg: String,
)

@JsonClass(generateAdapter = true)
data class DiscoverPostRequest(
        val msgTpye: String,
        val msg: String,
)

@JsonClass(generateAdapter = true)
data class DiscoverRequest(
        val lastUpdateTime : Long,
)

@JsonClass(generateAdapter = true)
data class DiscoverResponse(
        val success : Boolean,
        val discoverList: ArrayList<DiscoverInfo>,
)

@JsonClass(generateAdapter = true)
data class DiscoverInfo(
        val id: String,
        val text : String,
        val discoverType: String,
        val urlList : ArrayList<String>,
        val time: Long,
        val chatRecords : ArrayList<String>,
)

@JsonClass(generateAdapter = true)
data class DiscoverUserRequest(
        val discoverUser : String,
        val timePeriod : ArrayList<TimePair>
)

@JsonClass(generateAdapter = true)
data class TimePair(
        val prevTime : Long,
        val lastTime : Long,
)

@JsonClass(generateAdapter = true)
data class DiscoverUserResponse(
        val success: Boolean,
        val discoverList : ArrayList<DiscoverInfo>,
)
