package moe.kurenai.cq.netty.handle

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import moe.kurenai.cq.AbstractCQBot
import org.apache.logging.log4j.LogManager

class CqMessageReader(private val bot: AbstractCQBot) : SimpleChannelInboundHandler<TextWebSocketFrame>() {

    companion object {
        private val log = LogManager.getLogger()
    }

    override fun channelRead0(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) {
        bot.resolveIncoming(ctx, frame)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        log.warn("${bot.apiHost}:${bot.wsPort} was inactive")
    }
}