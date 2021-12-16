package io.github.kurenairyu.kurenaibot.netty

import io.github.kurenairyu.kurenaibot.KurenaiBot
import io.github.kurenairyu.kurenaibot.handler.ContinuationFrameHandler
import io.github.kurenairyu.kurenaibot.handler.CqMessageReader
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.stream.ChunkedWriteHandler

class WebSocketServerChannelInitializer(private val bot: KurenaiBot) : ChannelInitializer<SocketChannel>() {
    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        //websocket协议本身是基于http协议的，所以这边也要使用http解编码器
        pipeline.addLast(HttpServerCodec())
        //以块的方式来写的处理器
        pipeline.addLast(ChunkedWriteHandler())
        //netty是基于分段请求的，HttpObjectAggregator的作用是将请求分段再聚合,参数是聚合字节的最大长度
        pipeline.addLast(HttpObjectAggregator(65536))
        val path = "/ws"
        //ws://server:port/context_path
        //ws://localhost:9999/ws
        //参数指的是context_path
//        pipeline.addLast(new InitBotInfoHandler(path));
        pipeline.addLast(WebSocketServerProtocolHandler(path))
        //websocket定义了传递数据的6中frame类型
        pipeline.addLast(ContinuationFrameHandler::class.java.name, ContinuationFrameHandler())
        pipeline.addLast(CqMessageReader::class.java.name, CqMessageReader(bot))
    }
}