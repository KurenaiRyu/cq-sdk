package moe.kurenai.cq.event

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class Event {
    abstract val time: Long
    abstract val selfId: Long
    abstract val postType: PostType
}

enum class PostType(
    val type: String
) {

    MESSAGE("message"),
    NOTICE("notice"),
    REQUEST("request"),
    META("meta_event"),

}


@Serializable
abstract class NoticeEvent : Event() {
    abstract val noticeType: NoticeType
    override val postType = PostType.NOTICE
}

@Serializable
enum class NoticeType(
    val type: String
) {
    @SerialName("group_recall")
    GROUP_RECALL("group_recall"),
    @SerialName("group_increase")
    GROUP_INCREASE("group_increase"),
    @SerialName("group_upload")
    GROUP_UPLOAD("group_upload"),
}