package io.github.kurenairyu.kurenaibot.event

class MemberIncreaseEvent(
    override val time: Long,
    override val selfId: Long,
    override val groupId: Long,
    val subType: SubType,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(), GroupEvent {
    override val noticeType = NoticeType.group_increase
}

enum class SubType {
    approve,
    invite
}
