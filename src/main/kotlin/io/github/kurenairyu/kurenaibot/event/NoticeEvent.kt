package io.github.kurenairyu.kurenaibot.event

abstract class NoticeEvent : Event() {
    override val postType = PostType.NOTICE
    abstract val noticeType: String
}

object NoticeType {
    const val FIELD_NAME = "notice_type"
    const val GROUP_INCREASE = "group_increase"
}