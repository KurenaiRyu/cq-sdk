package io.github.kurenairyu.kurenaibot.event

class MemberIncreaseEvent(
    override val time: Long,
    override val selfId: Long,
    override val noticeType: NoticeType,
    override val groupId: Long,
    val subType: SubType,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(), GroupEvent {
}

enum class SubType(val value: String) {
    APPROVE("approve"),
    INVITE("invite")
}
