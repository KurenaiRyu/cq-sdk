package io.github.kurenairyu.kurenaibot.entity

import io.github.kurenairyu.kurenaibot.request.SendGroupMsg
import io.github.kurenairyu.kurenaibot.request.SendPrivateMsg

class Message(text: String? = null) {

    companion object {
        fun String.asMessage(): Message {
            return Message(this)
        }
    }

    private val list = ArrayList<SingleMessage>()

    init {
        text?.let { list.add(SingleMessage(MessageType.text, mapOf("text" to text))) }
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
        list.add(SingleMessage(MessageType.text, mapOf("text" to msg)))
        return this
    }

    fun at(qq: Long): Message {
        list.add(SingleMessage(MessageType.at, mapOf("qq" to qq.toString())))
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

enum class MessageType {
    text, image, face, at, reply, json;
}