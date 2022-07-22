package moe.kurenai.cq.model

import moe.kurenai.cq.request.group.SendGroupMsg
import moe.kurenai.cq.request.private.SendPrivateMsg
import java.io.File
import java.util.*

class MessageBuilder(text: String? = null) {

    companion object {
        fun String.asMessage(): MessageBuilder {
            return MessageBuilder(this)
        }
    }

    private val list = ArrayList<SingleMessage>()

    init {
        text?.let { list.add(SingleMessage(MessageType.TEXT, MessageData(text = text))) }
    }

    fun plus(msg: MessageBuilder): MessageBuilder {
        list.addAll(msg.list)
        return this
    }

    fun plus(msg: SingleMessage): MessageBuilder {
        list.add(msg)
        return this
    }

    fun plus(msg: String) = text(msg)

    fun text(msg: String): MessageBuilder {
        list.add(SingleMessage(MessageType.TEXT, MessageData(text = msg)))
        return this
    }

    fun at(qq: Long): MessageBuilder {
        list.add(SingleMessage(MessageType.AT, MessageData(qq = qq)))
        return this
    }

    fun img(path: String, isUrl: Boolean = false): MessageBuilder {
        list.add(SingleMessage(MessageType.IMAGE, MessageData(file = if (isUrl) path else "file:///$path")))
        return this
    }

    fun img(file: File): MessageBuilder {
        list.add(
            SingleMessage(
                MessageType.IMAGE,
                MessageData(file = "base64://${Base64.getEncoder().encode(file.readBytes()).decodeToString()}")
            )
        )
        return this
    }

    fun video(path: String, isUrl: Boolean = false): MessageBuilder {
        list.add(SingleMessage(MessageType.VIDEO, MessageData(file = if (isUrl) path else "file:///$path")))
        return this
    }

    fun record(path: String, isUrl: Boolean = false): MessageBuilder {
        list.add(SingleMessage(MessageType.RECORD, MessageData(file = if (isUrl) path else "file:///$path")))
        return this
    }

    fun privateMsg(qq: Long): SendPrivateMsg {
        return SendPrivateMsg(qq, list)
    }

    fun groupMsg(group: Long): SendGroupMsg {
        return SendGroupMsg(group, list)
    }

    fun build(): ArrayList<SingleMessage> {
        return list
    }
}

data class SingleMessage(val type: String, val data: MessageData)

data class MessageData(
    val id: Int? = null,
    val qq: Long? = null,
    val text: String? = null,
    val file: String? = null,
    val type: String? = null,
    val url: String? = null,
    val title: String? = null,
    val lat: Float? = null,
    val lon: Float? = null,
    val content: String? = null,
    val audio: String? = null,
    val user_id: Long? = null,
    val nickname: String? = null,
    val data: String? = null,
)

object MessageType {
    const val FIELD_NAME = "type"
    const val TEXT = "text"
    const val IMAGE = "image"
    const val FACE = "face"
    const val AT = "at"
    const val REPLY = "reply"
    const val JSON = "json"
    const val VIDEO = "video"
    const val FORWARD = "forward"
    const val RECORD = "record"
    const val RPS = "rps"
    const val DICE = "dice"
    const val SHAKE = "shake"
    const val POKE = "poke"
    const val SHARE = "share"
    const val CONTACT = "contact"
    const val MUSIC = "music"
    const val LOCATION = "location"
    const val NODE = "node"
}