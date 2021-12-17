package io.github.kurenairyu.kurenaibot

import io.github.kurenairyu.kurenaibot.config.CommonHeaderInterceptor
import io.github.kurenairyu.kurenaibot.event.Event
import io.github.kurenairyu.kurenaibot.netty.WebSocketChannelInitializer
import io.github.kurenairyu.kurenaibot.netty.WebSocketServerChannelInitializer
import io.github.kurenairyu.kurenaibot.request.SendGroupMsg
import io.github.kurenairyu.kurenaibot.request.SendPrivateMsg
import io.github.kurenairyu.kurenaibot.response.LoginInfo
import io.github.kurenairyu.kurenaibot.response.MessageId
import io.github.kurenairyu.kurenaibot.response.Receive
import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import mu.KotlinLogging
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.Map
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer

val log = KotlinLogging.logger {}

class KurenaiBot(
    val apiHost: String = "127.0.0.1",
    val httpPort: Int = 5700,
    val wsPort: Int = 6700,
    val accessToken: String = "",
    val bindPort: Int? = null,
) {

    companion object {
        fun SendGroupMsg.send(bot: KurenaiBot): Response<Receive<MessageId>> {
            return bot.api.sendGroupMsg(this).execute()
        }

        fun SendPrivateMsg.send(bot: KurenaiBot): Response<Receive<MessageId>> {
            return bot.api.sendPrivateMsg(this).execute()
        }
    }

    private val headerInterceptor = CommonHeaderInterceptor(Map.of("Authorization", "Bearer $accessToken"))
    private val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient().newBuilder() // 设置OKHttpClient,如果不设置会提供一个默认的
                .addInterceptor(headerInterceptor)
                .build()
        )
        .baseUrl("http://$apiHost:$httpPort") //设置baseUrl
        .addConverterFactory(JacksonConverterFactory.create(Constant.MAPPER))
        .build()
    val api = retrofit.create(CqAPI::class.java)
    var handlerList = ArrayList<Consumer<Event>>()
    val eventQueue = LinkedBlockingDeque<Event>()
    val running = AtomicBoolean(true)

    var loginInfo: LoginInfo? = null

    fun start(): KurenaiBot {
        //TODO: 获取好友列表，群组列表
        loginInfo = api.getLoginInfo().execute().body()?.data

        var bossGroup: NioEventLoopGroup? = null
        val workerGroup = NioEventLoopGroup()
        try {
            if (bindPort != null) {
                bossGroup = NioEventLoopGroup()
                val b = ServerBootstrap()
                b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel::class.java)
                    .childHandler(WebSocketServerChannelInitializer(this))
                    .bind(bindPort).sync()
                log.info("Web socket server start at port {}.", bindPort)
            } else {
                val bootStrap = Bootstrap()
                bootStrap
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .group(workerGroup)
                    .channel(NioSocketChannel::class.java)
                    .handler(WebSocketChannelInitializer(this))
                    .connect(apiHost, wsPort)
                    .sync().channel()
                log.info { "Web socket server connect to $apiHost:$wsPort." }
            }
        } catch (e: InterruptedException) {
            log.error(e) { "Ws服务启动失败。" }
        }

        val handlerThread = HandlerThread().also { it.name = "HandlerThread" }
        handlerThread.start()

        loginInfo?.let {
            log.info { "QQ ${it.nickname}(${it.userId}) 启动成功。" }
        }
        Runtime.getRuntime().addShutdownHook(Thread {
            bossGroup?.shutdownGracefully()
            workerGroup.shutdownGracefully()
        })
        return this
    }

    fun handle(handler: Consumer<Event>) {
        handlerList.add(handler)
    }

    private inner class HandlerThread : Thread() {
        override fun run() {
            priority = MIN_PRIORITY
            while (running.get()) {
                try {
                    val msg = eventQueue.take()
                    for (handler in handlerList) {
                        handler.accept(msg)
                    }
                } catch (e: InterruptedException) {
                    log.debug(e) { e.message }
                    interrupt()
                } catch (e: Exception) {
                    log.error(e) { e.message }
                }
            }
            log.debug { "Handler thread has being closed" }
        }
    }
}

fun main() {
//    val countDownLatch = CountDownLatch(1)
    val bot = KurenaiBot()
    bot.start()
//    countDownLatch.await()
}