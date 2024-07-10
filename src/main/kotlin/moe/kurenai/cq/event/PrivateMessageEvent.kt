package moe.kurenai.cq.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.kurenai.cq.model.SingleMessage

@Serializable
data class PrivateMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: List<SingleMessage>,
    override val rawMessage: String,
    override val font: Int,
    val subType: PrivateMessageEventSubType,
) : MessageEvent() {

    override val messageType = MessageEventType.PRIVATE

}

@Serializable
enum class PrivateMessageEventSubType(
    val type: String
) {
    @SerialName("group")
    GROUP("group"),
    @SerialName("group_self")
    GROUP_SELF("group_self"),
    @SerialName("other")
    OTHER("other"),
}
