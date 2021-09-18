package io.github.kurenairyu.cqsdk.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.github.kurenairyu.cqsdk.pojo.Event;
import io.github.kurenairyu.cqsdk.util.JacksonFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//处理文本协议数据，处理TextWebSocketFrame类型的数据，websocket专门处理文本的frame就是TextWebSocketFrame
@Slf4j
public class TextWebSocketFrameHandle extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final ObjectMapper    mapper = JacksonFactory.getInstance();
    private final ExecutorService pool   = new ThreadPoolExecutor(10, 30,
            30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder().setNameFormat("event-handler-{}").build());

    //读到客户端的内容并且向客户端去写内容
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Event event = null;
        log.debug(msg.text());
        try {
            event = mapper.readValue(msg.text(), Event.class);
            switch (event.getMessageType()) {
                case GROUP:
                    System.out.println("group");
//                    log.info("{}({}) - {}({}): {} ({})", Global.GROUP_INFO_MAP.get(event.getGroupId()).getGroupName(), event.getGroupId(), event.getSender().getNickname(), event.getSender().getUserId(), event.getRawMessage(), event.getMessageId());
                    break;
                case PRIVATE:
                    log.info("{}({}): {} ({})", event.getSender().getNickname(), event.getSender().getUserId(), event.getRawMessage(), event.getMessageId());
            }
        } catch (Exception e) {
            log.debug("Parse error! {}", msg.text());
        }
        if (event == null) return;


    }

    //每个channel都有一个唯一的id值
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //打印出channel唯一值，asLongText方法是channel的id的全名

        log.debug("handlerAdded：" + ctx.channel().id().asLongText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Global.removeBot(ctx);
        log.debug("handlerRemoved：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
//        ctx.close();
    }
}