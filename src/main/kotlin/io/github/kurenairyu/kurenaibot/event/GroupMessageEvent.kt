package io.github.kurenairyu.kurenaibot.event

import io.github.kurenairyu.kurenaibot.entity.Member
import io.github.kurenairyu.kurenaibot.entity.SingleMessage

class GroupMessageEvent(
    override val time: Long,
    override val selfId: Long,
    override val messageId: Int,
    override val userId: Long,
    override val message: List<SingleMessage>,
    override val rawMessage: String,
    override val font: Int,
    val anonymous: Anonymous? = null,
    val subType: GroupMessageSubType,
    val sender: Member,
    val groupId: Long,
) : MessageEvent() {
    override val messageType = MessageType.GROUP
}

enum class GroupMessageSubType(private val value: String) {
    NORMAL("normal"), ANONYMOUS("anonymous"), NOTICE("notice");

    override fun toString(): String {
        return value
    }
}

class Anonymous(
    val id: Long,
    val name: String,
    val flag: String,
)

enum class Role(val value: String) {
    OWNER("owner"), ADMIN("admin"), MEMBER("member");

    override fun toString(): String {
        return value
    }
}
