package moe.kurenai.cq.event.group

import moe.kurenai.cq.event.NoticeEvent
import moe.kurenai.cq.event.NoticeType

class GroupUploadFileEvent(
    override val time: Long,
    override val selfId: Long,
    override val groupId: Long,
    val subType: String,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(), GroupEvent {
    override val noticeType: String
        get() = NoticeType.GROUP_UPLOAD

}
