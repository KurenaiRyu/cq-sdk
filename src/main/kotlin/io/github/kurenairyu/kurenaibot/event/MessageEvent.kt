package io.github.kurenairyu.kurenaibot.event

import io.github.kurenairyu.kurenaibot.entity.SingleMessage

abstract class MessageEvent : Event() {
    override val postType = PostType.message
    abstract val messageType: MessageEventType
    abstract val messageId: Int
    abstract val userId: Long
    abstract val message: List<SingleMessage>
    abstract val rawMessage: String
    abstract val font: Int
}

enum class MessageEventType() {
    private, group;
}
