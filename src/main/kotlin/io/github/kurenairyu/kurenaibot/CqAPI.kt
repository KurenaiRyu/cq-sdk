package io.github.kurenairyu.kurenaibot

import io.github.kurenairyu.kurenaibot.request.SendGroupMsg
import io.github.kurenairyu.kurenaibot.request.SendPrivateMsg
import io.github.kurenairyu.kurenaibot.response.LoginInfo
import io.github.kurenairyu.kurenaibot.response.MessageId
import io.github.kurenairyu.kurenaibot.response.Receive
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CqAPI {


    @POST("send_group_msg")
    fun sendGroupMsg(@Body request: SendGroupMsg): Call<Receive<MessageId>>

    @POST("send_private_msg")
    fun sendPrivateMsg(@Body request: SendPrivateMsg): Call<Receive<MessageId>>

    @GET("get_login_info")
    fun getLoginInfo(): Call<Receive<LoginInfo>>

}