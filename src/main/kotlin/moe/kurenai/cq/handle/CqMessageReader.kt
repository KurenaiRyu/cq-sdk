package moe.kurenai.cq.handle

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import moe.kurenai.cq.KurenaiBot
import moe.kurenai.cq.event.MessageEventType
import moe.kurenai.cq.event.NoticeType
import moe.kurenai.cq.event.PostType
import moe.kurenai.cq.event.group.GroupMessageEvent
import moe.kurenai.cq.event.group.MemberIncreaseEvent
import moe.kurenai.cq.uritl.DefaultMapper.MAPPER
import org.apache.logging.log4j.LogManager

class CqMessageReader(private val bot: KurenaiBot) : SimpleChannelInboundHandler<TextWebSocketFrame>() {

    companion object {
        private val log = LogManager.getLogger()
    }

    override fun channelRead0(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) {
        try {
            val jsonNode = MAPPER.readTree(frame.text())
            when (val postType = jsonNode.findValue(PostType.FIELD_NAME).textValue()) {
                PostType.MESSAGE -> {
                    when (val messageEventType = jsonNode.findValue(MessageEventType.FIELD_NAME).textValue()) {
                        MessageEventType.GROUP -> {
                            MAPPER.treeToValue(jsonNode, GroupMessageEvent::class.java)
                        }
                        else -> {
                            log.warn("Unknown message event type $messageEventType:\n$jsonNode")
                            null
                        }
                    }
                }
                PostType.NOTICE -> {
                    when (val noticeType = jsonNode.findValue(NoticeType.FIELD_NAME).textValue()) {
                        NoticeType.GROUP_INCREASE -> {
                            MAPPER.treeToValue(jsonNode, MemberIncreaseEvent::class.java)
                        }
                        else -> {
                            log.warn("Unknown notice type $noticeType:\n$jsonNode")
                            null
                        }
                    }
                }
                else -> {
                    log.warn("Unknown post type $postType:\n$jsonNode")
                    null
                }
            }?.let {
                bot.publisher.submit(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.warn("${bot.apiHost}:${bot.wsPort} was inactive")
    }
}