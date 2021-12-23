package moe.kurenai.bot.kurenaibot.event

import moe.kurenai.bot.kurenaibot.entity.SingleMessage

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
