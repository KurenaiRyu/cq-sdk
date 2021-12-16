package io.github.kurenairyu.kurenaibot.event

abstract class Event {
    abstract val time: Long
    abstract val selfId: Long
    abstract val postType: PostType
}

enum class PostType(val value: String) {
    MESSAGE("message"),
    NOTICE("notice")
    ;

    override fun toString(): String {
        return value
    }


}