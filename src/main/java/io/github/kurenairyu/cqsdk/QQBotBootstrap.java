package io.github.kurenairyu.cqsdk;

import io.github.kurenairyu.cqsdk.core.WebSocketChannelInitializer;
import io.github.kurenairyu.cqsdk.exception.BotException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Kurenai
 * @since 2021-04-02 13:42
 */

@Slf4j
public class QQBotBootstrap {

    public static void main(String[] args) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, BotException {
        int port = 20010;
        start(port);
    }

    public static void start(int port) {
        NioEventLoopGroup bossGroup   = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            ChannelFuture channelFuture = b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new WebSocketChannelInitializer())
                    .bind(port).sync();

            channelFuture.channel().closeFuture().sync();

            log.info("Web socket server start at port {}.", port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
