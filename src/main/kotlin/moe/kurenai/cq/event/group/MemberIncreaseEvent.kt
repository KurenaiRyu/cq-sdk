package moe.kurenai.cq.event.group

import moe.kurenai.cq.event.NoticeEvent
import moe.kurenai.cq.event.NoticeType

class MemberIncreaseEvent(
    override val time: Long,
    override val selfId: Long,
    override val groupId: Long,
    val subType: String,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(), GroupEvent {
    override val noticeType = NoticeType.GROUP_INCREASE
}

object SubType {
    const val FIELD_NAME = "sub_type"
    const val APPROVE = "approve"
    const val INVITE = "invite"
}
