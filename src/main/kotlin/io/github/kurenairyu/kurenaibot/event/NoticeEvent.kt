package io.github.kurenairyu.kurenaibot.event

abstract class NoticeEvent : Event() {
    override val postType = PostType.NOTICE
    abstract val noticeType: NoticeType
}

enum class NoticeType(private val value: String) {
    GROUP_INCREASE("group_increase");

    override fun toString(): String {
        return value
    }
}