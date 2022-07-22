package moe.kurenai.cq

import com.fasterxml.jackson.databind.JsonNode
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import kotlinx.coroutines.*
import moe.kurenai.cq.event.Event
import moe.kurenai.cq.model.LoginInfo
import moe.kurenai.cq.model.ResponseWrapper
import moe.kurenai.cq.request.GetLoginInfo
import moe.kurenai.cq.request.Request
import moe.kurenai.cq.request.wrap
import moe.kurenai.cq.uritl.DefaultMapper.MAPPER
import org.apache.logging.log4j.LogManager
import java.util.concurrent.*
import kotlin.coroutines.resumeWithException

class CoroutineCQBot @JvmOverloads constructor(
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
    private val requestMap =
        ConcurrentHashMap<String, Pair<Request<Any>, CancellableContinuation<ResponseWrapper<Any>>>>()
    private val publisherPool: Executor
    private val publisherDispatcher: CoroutineDispatcher

    init {
        subscribers.forEach { subscriber: AbstractEventSubscriber -> this.publisher.subscribe(subscriber) }
        publisherPool = publisher.executor
        publisherDispatcher = publisherPool.asCoroutineDispatcher()
    }

    suspend fun start() {
        initConnectionOrServer()
        loginInfo = send(GetLoginInfo()) ?: throw Exception("QQ 启动失败")
        log.info("Start QQ [${loginInfo.nickname}](${loginInfo.userId}) success.")
    }

    fun <E : Event> addHandler(clazz: Class<E>, block: suspend (E) -> Unit) {
        val subscriber = object : AbstractEventSubscriber() {
            override fun onNext0(event: Event) {
                if (clazz.isInstance(event)) {
                    CoroutineScope(publisherDispatcher).launch {
                        block.invoke(clazz.cast(event))
                    }
                }
            }
        }
        publisher.subscribe(subscriber)
    }

    fun addSubscriber(subscriber: AbstractEventSubscriber) {
        publisher.subscribe(subscriber)
    }

    override fun resolveResponse(ctx: ChannelHandlerContext, json: String, requestId: String) {
        if (requestMap.containsKey(requestId)) {
            val (req, con) = requestMap[requestId]!!
            try {
                con as CancellableContinuation<Any>
                val response = MAPPER.readValue(json, req.responseType)!!
                con.resumeWith(Result.success(response))
            } catch (e: Exception) {
                con.resumeWithException(e)
            }
        }
    }

    override fun resolveEvent(ctx: ChannelHandlerContext, jsonNode: JsonNode) {
        publisher.submit(parseEvent(jsonNode))
    }

    suspend fun <T : Any> send(request: Request<T>, timeout: Long = 10, timeUnit: TimeUnit = TimeUnit.SECONDS): T? {
        checkChannelStatus()
        return suspendCancellableCoroutine<ResponseWrapper<T>> { con ->
            kotlin.runCatching {
                val requestJson = MAPPER.writeValueAsString(request.wrap())
                log.debug("Request: $requestJson")
                channel.writeAndFlush(TextWebSocketFrame(requestJson))
                val pair = (request to con) as Pair<Request<Any>, CancellableContinuation<ResponseWrapper<Any>>>
                requestMap[request.echo] = pair
                channel.eventLoop().schedule({
                    requestMap.remove(request.echo)?.run {
                        if (con.isActive)
                            con.cancel(
                                TimeoutException(
                                    "Request [${request.echo}] timeout in ${
                                        timeUnit.toSeconds(
                                            timeout
                                        )
                                    }s"
                                )
                            )
                    }
                }, timeout, timeUnit)
            }.recover {
                con.resumeWithException(it)
            }
        }.data
    }
}
