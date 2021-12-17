package io.github.kurenairyu.kurenaibot.event

import io.github.kurenairyu.kurenaibot.entity.Member
import io.github.kurenairyu.kurenaibot.entity.SingleMessage

class GroupMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: List<SingleMessage>,
    override val rawMessage: String,
    override val font: Int,
    override val groupId: Long,
    val anonymous: Anonymous? = null,
    val subType: GroupMessageSubType,
    val sender: Member,
) : MessageEvent(), GroupEvent {
    override val messageType = MessageEventType.group
}

enum class GroupMessageSubType {
    normal, anonymous, notice;
}

class Anonymous(
    val id: Long,
    val name: String,
    val flag: String,
)
