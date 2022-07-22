package moe.kurenai.cq.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import moe.kurenai.cq.event.group.GroupRecallEvent

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "notice_type",
    visible = true
)
@JsonSubTypes(
    value = [
        JsonSubTypes.Type(value = GroupRecallEvent::class, name = "group_increase"),
        JsonSubTypes.Type(value = GroupRecallEvent::class, name = "group_recall"),
    ]
)
abstract class NoticeEvent : Event() {
    abstract val noticeType: String
    override val postType: String = PostType.NOTICE
}

object NoticeType {
    const val FIELD_NAME = "notice_type"
    const val GROUP_RECALL = "group_recall"
    const val GROUP_INCREASE = "group_increase"
    const val GROUP_UPLOAD = "group_upload"
}