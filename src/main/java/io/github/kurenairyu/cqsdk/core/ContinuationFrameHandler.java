package io.github.kurenairyu.cqsdk.core;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContinuationFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private StringBuilder frameBuffer = null;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        log.debug("Received incoming frame [{}]", frame.getClass().getName());

        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof PongWebSocketFrame) {
            log.info("Pong frame received");
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            frameBuffer = new StringBuilder();
            frameBuffer.append(((TextWebSocketFrame) frame).text());
        } else if (frame instanceof ContinuationWebSocketFrame) {
            if (frameBuffer != null) {
                frameBuffer.append(((ContinuationWebSocketFrame) frame).text());
            } else {
                log.warn("Continuation frame received without initial frame.");
            }
        }

        // Check if Text or Continuation Frame is final fragment and handle if needed.
        if (frame.isFinalFragment()) {
            ctx.fireChannelRead(new TextWebSocketFrame(frameBuffer.toString()));
            frameBuffer = null;
        }
    }
}