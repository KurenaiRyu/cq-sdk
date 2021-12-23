package moe.kurenai.bot.kurenaibot

import moe.kurenai.bot.kurenaibot.request.GetGroupMemberInfo
import moe.kurenai.bot.kurenaibot.request.SendGroupMsg
import moe.kurenai.bot.kurenaibot.request.SendPrivateMsg
import moe.kurenai.bot.kurenaibot.response.GroupMemberInfo
import moe.kurenai.bot.kurenaibot.response.LoginInfo
import moe.kurenai.bot.kurenaibot.response.MessageId
import moe.kurenai.bot.kurenaibot.response.Receive
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