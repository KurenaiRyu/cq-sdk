package moe.kurenai.cq.request.group

import kotlinx.serialization.Serializable
import moe.kurenai.cq.model.MessageBuilder
import moe.kurenai.cq.model.MessageId
import moe.kurenai.cq.model.SingleMessage
import moe.kurenai.cq.request.Request

@Suppress("unused")
@Serializable
data class SendGroupMsg @JvmOverloads constructor(
    val groupId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
) : Request<MessageId>(
    path = PATH
) {
    companion object {
        val PATH = "send_group_msg"
    }

    constructor(
        groupId: Long,
        autoEscape: Boolean? = null,
        messageBuilder: MessageBuilder.() -> Unit)
            : this(groupId = groupId, autoEscape = autoEscape, message = MessageBuilder().apply(messageBuilder).build())
}