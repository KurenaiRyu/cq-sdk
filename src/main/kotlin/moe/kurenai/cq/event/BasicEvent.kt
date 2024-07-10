package moe.kurenai.cq.event

import kotlinx.serialization.Serializable

@Serializable
data class BasicEvent(
    val postType: PostType,
    val noticeType: NoticeType? = null,
    val messageType: MessageEventType? = null,
)