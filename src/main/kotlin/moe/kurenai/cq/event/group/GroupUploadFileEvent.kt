package moe.kurenai.cq.event.group

import kotlinx.serialization.Serializable
import moe.kurenai.cq.event.NoticeEvent
import moe.kurenai.cq.event.NoticeType

@Serializable
class GroupUploadFileEvent (
    override val time: Long,
    override val selfId: Long,
    override val groupId: Long,
    val subType: String,
    val operatorId: Long,
    val userId: Long
) : NoticeEvent(), GroupEvent {
    override val noticeType = NoticeType.GROUP_UPLOAD

}
