package io.github.kurenairyu.kurenaibot.netty

import io.github.kurenairyu.kurenaibot.KurenaiBot
import io.github.kurenairyu.kurenaibot.handler.CqMessageReader
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler
import io.netty.handler.codec.http.websocketx.WebSocketVersion
import java.net.URI

class WebSocketChannelInitializer(private val bot: KurenaiBot) : ChannelInitializer<SocketChannel>() {
    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast(HttpClientCodec())
        //以块的方式来写的处理器
//        pipeline.addLast(ChunkedWriteHandler())
        //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast(HttpObjectAggregator(65536))
        pipeline.addLast(
            WebSocketClientProtocolHandler(
                WebSocketClientHandshakerFactory.newHandshaker(
                    URI("ws://${bot.api}:${bot.wsPort}/event"),
                    WebSocketVersion.V13,
                    null,
                    false,
                    DefaultHttpHeaders()
                )
            )
        )
        //websocket定义了传递数据的6中frame类型
//        pipeline.addLast(ContinuationFrameHandler::class.java.name, ContinuationFrameHandler())
        pipeline.addLast(CqMessageReader::class.java.name, CqMessageReader(bot))
    }
}