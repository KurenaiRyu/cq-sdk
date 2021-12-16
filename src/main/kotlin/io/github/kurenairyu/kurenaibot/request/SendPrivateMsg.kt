package io.github.kurenairyu.kurenaibot.request

import io.github.kurenairyu.kurenaibot.entity.SingleMessage

class SendPrivateMsg(
    val userId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
)