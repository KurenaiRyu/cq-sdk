package io.github.kurenairyu.kurenaibot.netty

import io.github.kurenairyu.kurenaibot.Constant.Companion.MAPPER
import io.github.kurenairyu.kurenaibot.KurenaiBot
import io.github.kurenairyu.kurenaibot.event.GroupMessageEvent
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame

class CqMessageReader(private val bot: KurenaiBot) : SimpleChannelInboundHandler<TextWebSocketFrame>() {
    override fun channelRead0(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) {
        try {
            val jsonNode = MAPPER.readTree(frame.text())
            when (jsonNode.findValue("post_type").textValue()) {
                "message" -> {
                    when (jsonNode.findValue("message_type").textValue()) {
                        "group" -> {
                            MAPPER.treeToValue(jsonNode, GroupMessageEvent::class.java)
                        }
                        else -> null
                    }
                }
                else -> null
            }?.let(bot.eventQueue::add)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ctx.fireChannelRead(frame.retain())
        }
    }


}