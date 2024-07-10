package moe.kurenai.cq.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.kurenai.cq.model.SingleMessage

@Serializable
abstract class MessageEvent : Event() {
    override val postType = PostType.MESSAGE
    abstract val messageType: MessageEventType
    abstract val messageId: Int
    abstract val userId: Long
    abstract val message: List<SingleMessage>
    abstract val rawMessage: String
    abstract val font: Int
}

@Serializable
enum class MessageEventType(
    val type: String,
) {
    @SerialName("private")
    PRIVATE("private"),
    @SerialName("group")
    GROUP("group"),
}
