package moe.kurenai.cq

import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.util.concurrent.Promise
import moe.kurenai.cq.event.*
import moe.kurenai.cq.model.LoginInfo
import moe.kurenai.cq.request.GetLoginInfo
import moe.kurenai.cq.request.Request
import moe.kurenai.cq.request.wrap
import moe.kurenai.cq.uritl.DefaultMapper.MAPPER
import org.apache.logging.log4j.LogManager
import java.util.concurrent.*

class DefaultCQBot @JvmOverloads constructor(
    override val apiHost: String = "127.0.0.1",
    override val wsPort: Int = 6700,
    override val token: String? = null,
    override val bindPort: Int? = null,
    subscribers: List<AbstractEventSubscriber> = emptyList(),
    private val publisher: SubmissionPublisher<Event> = SubmissionPublisher<Event>(
        defaultPublishPool(),
        Flow.defaultBufferSize()
    ),
) : AbstractCQBot() {
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

    lateinit var loginInfo: LoginInfo
    val requestMap = ConcurrentHashMap<String, Pair<Request<Any>, Any>>()

    init {
        subscribers.forEach { subscriber: AbstractEventSubscriber -> this.publisher.subscribe(subscriber) }
    }

    fun start() {
        initConnectionOrServer()
        loginInfo = send(GetLoginInfo()).join() ?: throw Exception("QQ 启动失败")
        log.info("Start QQ [${loginInfo.nickname}](${loginInfo.userId}) success.")
    }

    fun <E : Event> addHandler(clazz: Class<E>, handler: (E) -> Unit) {
        val subscriber = object : AbstractEventSubscriber() {
            override fun onNext0(event: Event) {
                if (clazz.isInstance(event)) {
                    handler.invoke(clazz.cast(event))
                }
            }
        }
        publisher.subscribe(subscriber)
    }

    fun addSubscriber(subscriber: AbstractEventSubscriber) {
        publisher.subscribe(subscriber)
    }

    @Suppress("UNCHECKED_CAST")
    override fun resolveResponse(ctx: ChannelHandlerContext, json: String, requestId: String) {
        if (requestMap.containsKey(requestId)) {
            val (req, future) = requestMap[requestId]!!
            try {
                val response = MAPPER.readValue(json, req.responseType)!!
                when (future) {
                    is Promise<*> -> {
                        future as Promise<Any>
                        future.trySuccess(response.data)
                    }
                    is CompletableFuture<*> -> {
                        future as CompletableFuture<Any>
                        future.complete(response.data)
                    }
                }
            } catch (e: Exception) {
                when (future) {
                    is Promise<*> -> {
                        future as Promise<Any>
                        future.tryFailure(e)
                    }
                    is CompletableFuture<*> -> {
                        future as CompletableFuture<Any>
                        future.completeExceptionally(e)
                    }
                }
            }
        }
    }

    override fun resolveEvent(ctx: ChannelHandlerContext, jsonNode: JsonNode) {
        when (val postType = jsonNode.findValue(PostType.FIELD_NAME)?.textValue()) {
            PostType.MESSAGE -> {
                MAPPER.treeToValue(jsonNode, MessageEvent::class.java)
            }
            PostType.NOTICE -> {
                MAPPER.treeToValue(jsonNode, NoticeEvent::class.java)
            }
            PostType.META -> {
                MAPPER.treeToValue(jsonNode, MetaEvent::class.java)
            }
            else -> {
                error("Unknown post type $postType:\n$jsonNode")
            }
        }?.let {
            publisher.submit(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> promiseSend(
        request: Request<T>,
        timeout: Long = 10,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): Promise<T> {
        checkChannelStatus()
        val requestJson = MAPPER.writeValueAsString(request.wrap())
        log.debug("Request: $requestJson")
        channel.writeAndFlush(TextWebSocketFrame(requestJson))
        val promise = channel.eventLoop().newPromise<T>()
        requestMap[request.echo] = (request to promise) as Pair<Request<Any>, Any>
        channel.eventLoop().schedule({
            requestMap.remove(request.echo)?.run {
                if (!promise.isDone) {
                    promise.cancel(false)
                    promise.tryFailure(
                        TimeoutException(
                            "Request [${request.echo}] timeout in ${
                                timeUnit.toSeconds(
                                    timeout
                                )
                            }s"
                        )
                    )
                }

            }
        }, timeout, timeUnit)
        return promise
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> send(
        request: Request<T>,
        timeout: Long = 10,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): CompletableFuture<T> {
        checkChannelStatus()
        val requestJson = MAPPER.writeValueAsString(request.wrap())
        log.debug("Request: $requestJson")
        channel.writeAndFlush(TextWebSocketFrame(requestJson))
        val future = CompletableFuture<T>()
        requestMap[request.echo] = (request to future) as Pair<Request<Any>, Any>
        channel.eventLoop().schedule({
            requestMap.remove(request.echo)?.run {
                if (!future.isDone) {
                    future.cancel(false)
                    future.completeExceptionally(
                        TimeoutException(
                            "Request [${request.echo}] timeout in ${
                                timeUnit.toSeconds(
                                    timeout
                                )
                            }s"
                        )
                    )
                }

            }
        }, timeout, timeUnit)
        return future
    }
}