package moe.kurenai.bot.kurenaibot.request

import moe.kurenai.bot.kurenaibot.entity.SingleMessage

class SendPrivateMsg(
    val userId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
)