package moe.kurenai.bot.kurenaibot.event

import moe.kurenai.bot.kurenaibot.entity.SingleMessage

data class PrivateMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: List<SingleMessage>,
    override val rawMessage: String,
    override val font: Int
) : MessageEvent() {

    override val messageType = MessageEventType.GROUP

}