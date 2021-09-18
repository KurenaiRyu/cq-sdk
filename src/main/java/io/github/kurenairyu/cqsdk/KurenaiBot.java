package io.github.kurenairyu.cqsdk;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class KurenaiBot extends BaseBot {

    private final String xClientRole;
    private final long   qq;
    private       String nickname;

    public KurenaiBot(ChannelHandlerContext ctx, FullHttpRequest request, long qq, String xClientRole) {
        super(ctx, request);
        this.qq = qq;
        this.xClientRole = xClientRole;
    }

    public Object SendMessage() {
        return null;
    }
}
