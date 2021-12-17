package io.github.kurenairyu.kurenaibot.event

abstract class NoticeEvent : Event() {
    override val postType = PostType.notice
    abstract val noticeType: NoticeType
}

enum class NoticeType {
    group_increase;
}