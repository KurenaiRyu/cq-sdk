package io.github.kurenairyu.kurenaibot

import io.github.kurenairyu.kurenaibot.request.GetGroupMemberInfo
import io.github.kurenairyu.kurenaibot.request.SendGroupMsg
import io.github.kurenairyu.kurenaibot.request.SendPrivateMsg
import io.github.kurenairyu.kurenaibot.response.GroupMemberInfo
import io.github.kurenairyu.kurenaibot.response.LoginInfo
import io.github.kurenairyu.kurenaibot.response.MessageId
import io.github.kurenairyu.kurenaibot.response.Receive
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CqAPI {


    @POST("send_group_msg")
    fun sendGroupMsg(@Body request: SendGroupMsg): Call<Receive<MessageId>>

    @POST("send_private_msg")
    fun sendPrivateMsg(@Body request: SendPrivateMsg): Call<Receive<MessageId>>

    @GET("get_login_info")
    fun getLoginInfo(): Call<Receive<LoginInfo>>

    @GET("get_group_member_info")
    fun getGroupMemberInfo(@Body request: GetGroupMemberInfo): Call<Receive<GroupMemberInfo>>

    @GET("get_group_member_list")
    fun getGroupMemberList(@Query("group_id") groupId: Long): Call<Receive<List<GroupMemberInfo>>>

}