package io.github.kurenairyu.cqsdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.kurenairyu.cqsdk.util.JacksonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseBot {

    private static final Logger log = LoggerFactory.getLogger(BaseBot.class);

    private final ChannelHandlerContext ctx;
    private final FullHttpRequest       request;
    private final ObjectMapper          mapper = JacksonFactory.getInstance();

    public BaseBot(ChannelHandlerContext ctx, FullHttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public void writeAndFlush(Object obj) {
        try {
            ctx.writeAndFlush(new TextWebSocketFrame(mapper.writeValueAsString(obj)));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void fireChannelRead() {
        ctx.fireChannelRead(request.retain());
    }
}
