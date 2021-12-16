package io.github.kurenairyu.kurenaibot.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.*
import mu.KotlinLogging

class ContinuationFrameHandler : SimpleChannelInboundHandler<WebSocketFrame>() {
    private val log = KotlinLogging.logger {}

    private var frameBuffer: StringBuilder? = null

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, frame: WebSocketFrame) {
        log.debug("Received incoming frame [{}]", frame.javaClass.name)
        if (frame is PingWebSocketFrame) {
            ctx.channel().writeAndFlush(PongWebSocketFrame(frame.content().retain()))
            return
        }
        if (frame is PongWebSocketFrame) {
            log.info("Pong frame received")
            return
        }
        if (frame is TextWebSocketFrame) {
            frameBuffer = StringBuilder()
            frameBuffer?.append(frame.text())
        } else if (frame is ContinuationWebSocketFrame) {
            if (frameBuffer != null) {
                frameBuffer!!.append(frame.text())
            } else {
                log.warn("Continuation frame received without initial frame.")
            }
        }

        // Check if Text or Continuation Frame is final fragment and handle if needed.
        if (frame.isFinalFragment) {
            ctx.fireChannelRead(TextWebSocketFrame(frameBuffer.toString()))
            frameBuffer = null
        }
    }
}