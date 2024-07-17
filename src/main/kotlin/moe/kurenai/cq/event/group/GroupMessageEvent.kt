package moe.kurenai.cq.event.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.kurenai.cq.event.MessageEvent
import moe.kurenai.cq.event.MessageEventType
import moe.kurenai.cq.model.Member
import moe.kurenai.cq.model.MessageChain
import moe.kurenai.cq.model.SingleMessage

@Serializable
data class GroupMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: MessageChain,
    override val rawMessage: String,
    override val font: Int,
    override val groupId: Long,
    val sender: Member,
    val subType: GroupMessageSubType,
    val anonymous: Anonymous? = null,
) : MessageEvent(), GroupEvent {
    override val messageType = MessageEventType.GROUP
}

@Serializable
enum class GroupMessageSubType (
    val type: String
) {
    @SerialName("normal")
    NORMAL("normal"),
    @SerialName("anonymous")
    ANONYMOUS("anonymous"),
    @SerialName("notice")
    NOTICE("notice"),
}

@Serializable
class Anonymous(
    val id: Long,
    val name: String,
    val flag: String,
)
