package moe.kurenai.cq

import com.fasterxml.jackson.databind.JsonNode
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import moe.kurenai.cq.event.*
import moe.kurenai.cq.netty.WebSocketChannelInitializer
import moe.kurenai.cq.netty.WebSocketServerChannelInitializer
import moe.kurenai.cq.util.DefaultMapper
import org.apache.logging.log4j.LogManager
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author Kurenai
 * @since 7/21/2022 11:19:10
 */

abstract class AbstractCQBot {

    companion object {
        private val log = LogManager.getLogger()
    }

    abstract val apiHost: String
    abstract val wsPort: Int
    abstract val bindPort: Int?
    abstract val token: String?
    protected lateinit var channel: SocketChannel

    var running = AtomicBoolean(false)

    protected fun checkChannelStatus() {
        if (!channel.isActive) error("Channel(${channel.remoteAddress()}) was inactive.")
    }

    protected fun initConnectionOrServer() {
        running.set(true)

        var bossGroup: NioEventLoopGroup? = null
        val workerGroup = NioEventLoopGroup()
        Runtime.getRuntime().addShutdownHook(Thread {
            bossGroup?.shutdownGracefully()
            workerGroup.shutdownGracefully()
        })
        try {
            if (bindPort != null) {
                bossGroup = NioEventLoopGroup()
                val b = ServerBootstrap()
                b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(WebSocketServerChannelInitializer(this))
                    .bind(bindPort!!).sync()
                log.info("Web socket server start at port {}.", bindPort)
            } else {
                val bootStrap = Bootstrap()
                val future = bootStrap
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .group(workerGroup)
                    .channel(NioSocketChannel::class.java)
                    .handler(WebSocketChannelInitializer(this))
                    .connect(apiHost, wsPort).addListener {
                        if (!it.isSuccess) {
                            doReconnect(bootStrap, 3)
                        }
                    }
                future.await()
                channel = future.sync().channel() as SocketChannel
                channel.closeFuture().addListener { feture ->
                    feture.cause()?.let {
                        log.error("Web socket connection closed.", it.cause)
                        log.info("Reconnecting...")
                    }
                    doReconnect(bootStrap)
                }

                log.info("Web socket server connect to $apiHost:$wsPort.")
            }
        } catch (e: InterruptedException) {
            running.set(false)
            throw Error("Start web socket server failed.", e)
        }
    }

    protected fun doReconnect(bootstrap: Bootstrap, delay: Long = 5, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        bootstrap.connect(apiHost, wsPort).addListener { f ->
            f as ChannelFuture
            if (!f.isSuccess) {
                log.info("Connecting failed，reconnecting $apiHost:$wsPort")
                f.channel().eventLoop().schedule({
                    doReconnect(bootstrap)
                }, delay, timeUnit)
            } else {
                log.info("Reconnecting success.")
            }
        }
    }

    open fun resolveIncoming(ctx: ChannelHandlerContext, frame: TextWebSocketFrame) {
        log.debug("Incoming: ${frame.text().trim()}")
        try {
            val json = frame.text()
            val jsonNode = DefaultMapper.MAPPER.readTree(json)
            val requestId = jsonNode.findValue("echo")?.textValue()
            if (requestId != null) {
                resolveResponse(ctx, json, requestId)
            } else {
                resolveEvent(ctx, jsonNode)
            }
        } catch (e: Exception) {
            log.error("Error while reading message: $frame", e)
        }
    }

    protected fun parseEvent(jsonNode: JsonNode): Event {
        return when (val postType = jsonNode.findValue(PostType.FIELD_NAME)?.textValue()) {
            PostType.MESSAGE -> {
                DefaultMapper.MAPPER.treeToValue(jsonNode, MessageEvent::class.java)
            }
            PostType.NOTICE -> {
                DefaultMapper.MAPPER.treeToValue(jsonNode, NoticeEvent::class.java)
            }
            PostType.META -> {
                DefaultMapper.MAPPER.treeToValue(jsonNode, MetaEvent::class.java)
            }
            else -> {
                error("Unknown post type $postType:\n$jsonNode")
            }
        }
    }

    abstract fun resolveResponse(ctx: ChannelHandlerContext, json: String, requestId: String)
    abstract fun resolveEvent(ctx: ChannelHandlerContext, jsonNode: JsonNode)

}