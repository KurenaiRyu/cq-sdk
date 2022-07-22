package moe.kurenai.cq.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import moe.kurenai.cq.event.group.GroupMessageEvent
import moe.kurenai.cq.model.SingleMessage

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "message_type",
    visible = true
)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = GroupMessageEvent::class, name = "group"),
        JsonSubTypes.Type(value = PrivateMessageEvent::class, name = "private"),
    ]
)
abstract class MessageEvent : Event() {
    override val postType = PostType.MESSAGE
    abstract val messageType: String
    abstract val messageId: Int
    abstract val userId: Long
    abstract val message: List<SingleMessage>
    abstract val rawMessage: String
    abstract val font: Int
}

object MessageEventType {
    const val FIELD_NAME = "message_type"
    const val PRIVATE = "private"
    const val GROUP = "group"
}
