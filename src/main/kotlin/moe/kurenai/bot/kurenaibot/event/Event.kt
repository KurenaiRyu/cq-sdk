package moe.kurenai.bot.kurenaibot.event

abstract class Event {
    abstract val time: Long
    abstract val selfId: Long
    abstract val postType: String
}

object PostType {
    const val FIELD_NAME = "post_type"
    const val MESSAGE = "message"
    const val NOTICE = "notice"
}