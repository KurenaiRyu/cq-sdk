package io.github.kurenairyu.kurenaibot.event

abstract class Event {
    abstract val time: Long
    abstract val selfId: Long
    abstract val postType: PostType
}

enum class PostType() {
    message,
    notice
    ;
}