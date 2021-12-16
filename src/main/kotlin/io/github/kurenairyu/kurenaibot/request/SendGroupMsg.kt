package io.github.kurenairyu.kurenaibot.request

import io.github.kurenairyu.kurenaibot.entity.SingleMessage

class SendGroupMsg(
    val groupId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
)