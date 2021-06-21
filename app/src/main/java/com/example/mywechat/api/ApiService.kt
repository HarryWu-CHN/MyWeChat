package com.example.mywechat.api

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.BoolRes
import com.squareup.moshi.Json
import com.example.mywechat.repository.LoginRequest
import com.squareup.moshi.JsonClass
import dagger.hilt.internal.GeneratedEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File
import java.sql.Time

interface ApiService {
    @POST("user/logon")
    suspend fun register(@Body request: SignUpRequest) : BooleanResponse
    @POST("user/login")
    suspend fun httpLogin(@Body request: HttpLoginRequest) : BooleanResponse

    @Multipart
    @POST("user/edit")
    suspend fun userEdit(@Part("nickname") nickname: RequestBody?,
                         @Part icon: MultipartBody.Part?) : BooleanResponse

    @POST("user/get")
    suspend fun userGet(@Body request: UserGetRequest) : UserGetResponse

    // 好友相关
    @POST("contact/get")
    suspend fun contactGet() : ContactGetResponse
    @POST("contact/find")
    suspend fun contactFind(@Body request: ContactFindRequest) : ContactFindResponse

    @POST("contact/add")
    suspend fun contactAdd(@Body request: ContactAddRequest) : BooleanResponse

    @POST("contact/agree")
    suspend fun contactAgree(@Body request: ContactAgreeRequest) : BooleanResponse

    @POST("contact/waited")
    suspend fun contactWaited(@Body request: ContactWaitedText) : ContactFindResponse

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
    @Multipart
    @POST("chat/send")
    suspend fun chatSend(@Part("sendTo") sendTo: RequestBody,
                         @Part("msg") msg: RequestBody?,
                         @Part("msgType") msgType: RequestBody,
                         @Part file: MultipartBody.Part?) : BooleanResponse

    @Multipart
    @POST("discover/post")
    suspend fun discoverPost(@Part("msgType") msgType: RequestBody,
                             @Part("text") text: RequestBody,
                             @Part files: List<MultipartBody.Part>) : BooleanResponse
    //suspend fun discoverPost(@Body request: DiscoverPostRequest) : BooleanResponse

    @POST("discover")
    suspend fun discover(@Body request: DiscoverRequest) : DiscoverResponse

    @POST("discover/thumb")
    suspend fun thumb(@Body request: ThumbRequest) : BooleanResponse

    @POST("discover/comment")
    suspend fun comment(@Body request: CommentRequest) : BooleanResponse

    @POST("discover/user")
    suspend fun discoverUser(@Body request: DiscoverUserRequest) : DiscoverUserResponse

}

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    val username : String,
    val password : String,
)

@JsonClass(generateAdapter = true)
data class HttpLoginRequest(
        val username : String,
        val password : String,
)

@JsonClass(generateAdapter = true)
data class BooleanResponse (
    val success : Boolean,
    val time : Long,
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
data class ContactGetResponse(
        val friendNames: List<String>,
        val friendTypes: List<String>,
        val friendIcons: List<String>,
)

@JsonClass(generateAdapter = true)
data class ContactFindRequest(
        val userToFind : String,
)

@JsonClass(generateAdapter = true)
data class ContactFindResponse(
        val success : Boolean,
        val time : String,
        val userNames : List<String>,
        val userIcons : List<String>,
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
data class ContactWaitedText(
        val username: String,
)

@JsonClass(generateAdapter = true)
data class ChatSendResponse(
        val success: Boolean,
        val time : Long,
        val msgType : String,
        val msg : String?,
        val file : File?
)

@JsonClass(generateAdapter = true)
data class GroupCreateRequest(
        val groupName : String,
        val membersName : List<String>,
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
data class DiscoverPostRequest(
        val msgType: String,
        val msg: String,
        val files: List<File>?,
)

@JsonClass(generateAdapter = true)
data class DiscoverRequest(
        val lastUpdateTime : Long,
)

@JsonClass(generateAdapter = true)
data class DiscoverResponse(
        val success : Boolean,
        val discoverList: List<DiscoverInfo>,
)

@JsonClass(generateAdapter = true)
data class DiscoverInfo(
        val id: String,
        val username: String,
        val text : String,
        val discoverType: String,
        val urlList : List<String>?,
        val time: String,
        val thumbUsers: List<String>?,
        val discoverComments: List<DiscoverComment>?,
)

@JsonClass(generateAdapter = true)
data class DiscoverComment(
        val username: String,
        val sendTo: String?,
        val msg: String,
)

@JsonClass(generateAdapter = true)
data class ThumbRequest(
        val discoverId: String,
        val thumb: String,
)

@JsonClass(generateAdapter = true)
data class CommentRequest(
        val discoverId: String,
        val sendTo: String?,
        val msg: String,
)

@JsonClass(generateAdapter = true)
data class DiscoverUserRequest(
        val discoverUser : String,
        val timePeriod : List<TimePair>
)

@JsonClass(generateAdapter = true)
data class TimePair(
        val prevTime : Long,
        val lastTime : Long,
)

@JsonClass(generateAdapter = true)
data class DiscoverUserResponse(
        val success: Boolean,
        val discoverList : List<DiscoverInfo>,
)
