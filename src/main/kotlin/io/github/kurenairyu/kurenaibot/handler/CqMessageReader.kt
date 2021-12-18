package io.github.kurenairyu.kurenaibot.handler

import io.github.kurenairyu.kurenaibot.Constant.Companion.MAPPER
import io.github.kurenairyu.kurenaibot.KurenaiBot
import io.github.kurenairyu.kurenaibot.event.*
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class CqMessageReader(private val bot: KurenaiBot) : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    override fun channelRead0(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) {
        try {
            val jsonNode = MAPPER.readTree(frame.text())
            when (jsonNode.findValue(PostType.FIELD_NAME).textValue()) {
                PostType.MESSAGE -> {
                    when (jsonNode.findValue(MessageEventType.FIELD_NAME).textValue()) {
                        MessageEventType.GROUP -> {
                            MAPPER.treeToValue(jsonNode, GroupMessageEvent::class.java)
                        }
                        else -> null
                    }
                }
                PostType.NOTICE -> {
                    when (jsonNode.findValue(NoticeType.FIELD_NAME).textValue()) {
                        NoticeType.GROUP_INCREASE -> {
                            MAPPER.treeToValue(jsonNode, MemberIncreaseEvent::class.java)
                        }
                        else -> null
                    }
                }
                else -> null
            }?.let(bot.eventQueue::add)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}