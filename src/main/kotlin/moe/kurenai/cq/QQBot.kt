package moe.kurenai.cq

import io.netty.bootstrap.Bootstrap
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import moe.kurenai.cq.client.CQClient
import moe.kurenai.cq.event.Event
import moe.kurenai.cq.model.LoginInfo
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.netty.WebSocketChannelInitializer
import moe.kurenai.cq.netty.WebSocketServerChannelInitializer
import moe.kurenai.cq.request.GetLoginInfo
import org.apache.logging.log4j.LogManager
import java.util.concurrent.*

class KurenaiBot(
    subscribers: List<AbstractEventSubscriber>,
    val apiHost: String = "127.0.0.1",
    val httpPort: Int = 5700,
    val token: String? = null,
    val wsPort: Int = 6700,
    val client: CQClient = CQClient("http://$apiHost:$httpPort", token),
    val bindPort: Int? = null,
    val publisher: SubmissionPublisher<Event> = SubmissionPublisher<Event>(defaultPublishPool(), Flow.defaultBufferSize()),
) {
    companion object {
        private val log = LogManager.getLogger()

        private fun defaultPublishPool(): ThreadPoolExecutor {
            return ThreadPoolExecutor(
                1, 1, 1L, TimeUnit.MILLISECONDS,
                LinkedBlockingQueue(300)
            ) { r ->
                val t = Thread(
                    Thread.currentThread().threadGroup, r,
                    "event-publish",
                    0
                )
                if (t.isDaemon) t.isDaemon = false
                if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
                t
            }
        }
    }

    var loginInfo: LoginInfo
    var running = true

    init {
        subscribers.forEach { subscriber: AbstractEventSubscriber -> this.publisher.subscribe(subscriber) }
        //TODO: 获取好友列表，群组列表
        val loginInfoFuture: CompletableFuture<ResponseWrapper<LoginInfo>> = client.send(GetLoginInfo())

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
                    .connect(apiHost, wsPort).addListener {
                        if (!it.isSuccess) {
                            doReconnect(bootStrap, 3)
                        }
                    }
                    .sync().channel()
                    .closeFuture().addListener {
                        it.cause()?.let {
                            log.error("Web socket 连接关闭", it.cause)
                            log.info("重新连接...")
                        }
                        doReconnect(bootStrap)
                    }

                log.info("Web socket server connect to $apiHost:$wsPort.")
            }
        } catch (e: InterruptedException) {
            throw Exception("Ws服务启动失败。", e)
        }

        loginInfo = loginInfoFuture.join().takeIf { it.isSuccess() }?.data ?: throw Exception("QQ 启动失败")
        log.info("QQ ${loginInfo.nickname}(${loginInfo.userId}) 启动成功")
    }

    private fun doReconnect(bootstrap: Bootstrap, delay: Long = 5, timeUnit: TimeUnit = TimeUnit.SECONDS) {
        bootstrap.connect(apiHost, wsPort).addListener { f ->
            f as ChannelFuture
            if (!f.isSuccess) {
                log.info("连接失败，重新连接 $apiHost:$wsPort")
                f.channel().eventLoop().schedule({
                    doReconnect(bootstrap)
                }, delay, timeUnit)
            } else {
                log.info("重新连接成功")
            }
        }
    }
}

fun main() {
//    val uri = File("X:\\Pictures\\e6mS7XPq4JkzhVc_2.gif").toURI()
//    println(uri)
    val client = CQClient("http://121.4.170.11:5710", "gqEkxed8rFRmBXe1")
//
//    client.sendSync(Message().img(File("X:\\Pictures\\e6mS7XPq4JkzhVc_2.gif")).groupMsg(622032041))

//    val bot = KurenaiBot(listOf(DefaultEventSubscriber()), "121.4.170.11", 5710, "gqEkxed8rFRmBXe1", 6710)
    val bot = KurenaiBot(listOf(DefaultEventSubscriber()))
}