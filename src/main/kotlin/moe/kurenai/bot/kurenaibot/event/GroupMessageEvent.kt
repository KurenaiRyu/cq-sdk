package moe.kurenai.bot.kurenaibot.event

import moe.kurenai.bot.kurenaibot.entity.Member
import moe.kurenai.bot.kurenaibot.entity.SingleMessage

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
    val subType: String,
    val sender: Member,
) : MessageEvent(), GroupEvent {
    override val messageType = MessageEventType.GROUP
}

object GroupMessageSubType {
    const val FIELD_NAME = "subtype"
    const val NORMAL = "normal"
    const val ANONYMOUS = "anonymous"
    const val NOTICE = "notice";
}

class Anonymous(
    val id: Long,
    val name: String,
    val flag: String,
)
