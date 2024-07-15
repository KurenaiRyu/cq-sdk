package moe.kurenai.cq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import moe.kurenai.cq.request.group.SendGroupMsg
import moe.kurenai.cq.request.private.SendPrivateMsg
import java.io.File
import java.net.URI
import java.util.*

class MessageBuilder {

    private val list = ArrayList<SingleMessage>()

    companion object {
        fun of(text: String) {
            MessageBuilder().apply {
                plus(text)
            }
        }
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

    fun img(uri: URI): MessageBuilder {
        list.add(SingleMessage(MessageType.IMAGE, MessageData(file = uri.path)))
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

@Serializable
data class SingleMessage(val type: MessageType, val data: MessageData)

@Serializable
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

@Serializable
enum class MessageType {
    @SerialName("type")
    FIELD_NAME,
    @SerialName("text")
    TEXT,
    @SerialName("image")
    IMAGE,
    @SerialName("face")
    FACE,
    @SerialName("at")
    AT,
    @SerialName("reply")
    REPLY,
    @SerialName("json")
    JSON,
    @SerialName("video")
    VIDEO,
    @SerialName("forward")
    FORWARD,
    @SerialName("record")
    RECORD,
    @SerialName("rps")
    RPS,
    @SerialName("dice")
    DICE,
    @SerialName("shake")
    SHAKE,
    @SerialName("poke")
    POKE,
    @SerialName("share")
    SHARE,
    @SerialName("contact")
    CONTACT,
    @SerialName("music")
    MUSIC,
    @SerialName("location")
    LOCATION,
    @SerialName("node")
    NODE,
}