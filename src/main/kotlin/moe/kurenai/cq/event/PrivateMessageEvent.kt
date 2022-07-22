package moe.kurenai.cq.event

import moe.kurenai.cq.model.SingleMessage

data class PrivateMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: List<SingleMessage>,
    override val rawMessage: String,
    override val font: Int,
    val subType: String,
) : MessageEvent() {

    override val messageType get() = MessageEventType.PRIVATE

}

object PrivateMessageEventSubType {
    const val FRIEND = "friend"
    const val GROUP = "group"
    const val GROUP_SELF = "group_self"
    const val OTHER = "other"
}
