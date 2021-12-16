package io.github.kurenairyu.kurenaibot.entity

import io.github.kurenairyu.kurenaibot.request.SendGroupMsg
import io.github.kurenairyu.kurenaibot.request.SendPrivateMsg

class Message(text: String? = null) {
    private val list = ArrayList<SingleMessage>()

    init {
        text?.let { list.add(SingleMessage(MessageType.TEXT, mapOf("text" to text))) }
    }

    fun plus(msg: Message): Message {
        list.addAll(msg.list)
        return this
    }

    fun plus(msg: SingleMessage): Message {
        list.add(msg)
        return this
    }

    fun plus(msg: String) = text(msg)

    fun text(msg: String): Message {
        list.add(SingleMessage(MessageType.TEXT, mapOf("text" to msg)))
        return this
    }

    fun at(qq: Long): Message {
        list.add(SingleMessage(MessageType.AT, mapOf("qq" to qq.toString())))
        return this
    }

    fun privateMsg(qq: Long): SendPrivateMsg {
        return SendPrivateMsg(qq, list)
    }

    fun groupMsg(group: Long): SendGroupMsg {
        return SendGroupMsg(group, list)
    }
}

class SingleMessage(val type: MessageType, val data: Map<String, String>)

enum class MessageType(private val value: String) {
    TEXT("text"), IMAGE("image"), FACE("face"), AT("at");

    override fun toString(): String {
        return this.value
    }
}