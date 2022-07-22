package moe.kurenai.cq.event.group

import moe.kurenai.cq.event.MessageEvent
import moe.kurenai.cq.event.MessageEventType
import moe.kurenai.cq.model.Member
import moe.kurenai.cq.model.SingleMessage

data class GroupMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: List<SingleMessage>,
    override val rawMessage: String,
    override val font: Int,
    override val groupId: Long,
    val sender: Member,
    val subType: String,
    val anonymous: Anonymous? = null,
) : MessageEvent(), GroupEvent {
    override val messageType: String get() = MessageEventType.GROUP
}

object GroupMessageSubType {
    const val NORMAL = "normal"
    const val ANONYMOUS = "anonymous"
    const val NOTICE = "notice";
}

class Anonymous(
    val id: Long,
    val name: String,
    val flag: String,
)
