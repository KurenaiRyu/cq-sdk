package moe.kurenai.cq.event

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonClassDiscriminator
import moe.kurenai.cq.event.group.GroupRecallEvent

sealed class Event {
    abstract val time: Long
    abstract val selfId: Long
    abstract val postType: String
}

object PostType {
    const val FIELD_NAME = "post_type"
    const val MESSAGE = "message"
    const val NOTICE = "notice"
    const val REQUEST = "request"
    const val META = "meta_event"
}


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

@JsonClassDiscriminator(PostType.FIELD_NAME)
@SerialName(PostType.NOTICE)
abstract class NoticeEvent : Event() {
    abstract val noticeType: String
}

object NoticeType {
    const val FIELD_NAME = "notice_type"
    const val GROUP_RECALL = "group_recall"
    const val GROUP_INCREASE = "group_increase"
    const val GROUP_UPLOAD = "group_upload"
}