package moe.kurenai.cq.request.private

import kotlinx.serialization.Serializable
import moe.kurenai.cq.model.MessageBuilder
import moe.kurenai.cq.model.MessageId
import moe.kurenai.cq.model.SingleMessage
import moe.kurenai.cq.request.Request

@Suppress("unused")
@Serializable
class SendPrivateMsg @JvmOverloads constructor(
    val userId: Long,
    val message: List<SingleMessage>,
    val autoEscape: Boolean? = null,
) : Request<MessageId>(
    path = "send_private_msg",
) {

    constructor(
        userId: Long,
        autoEscape: Boolean? = null,
        messageBuilder: MessageBuilder.() -> Unit)
            : this(userId = userId, autoEscape = autoEscape, message = MessageBuilder().apply(messageBuilder).build())
}