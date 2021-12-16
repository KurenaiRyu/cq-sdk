package io.github.kurenairyu.kurenaibot.event

import io.github.kurenairyu.kurenaibot.entity.SingleMessage

abstract class MessageEvent : Event() {
    override val postType = PostType.MESSAGE
    abstract val messageType: MessageType
    abstract val messageId: Int
    abstract val userId: Long
    abstract val message: List<SingleMessage>
    abstract val rawMessage: String
    abstract val font: Int
}

enum class MessageType(private val value: String) {
    PRIVATE("private"), GROUP("group");

    override fun toString(): String {
        return value
    }
}
