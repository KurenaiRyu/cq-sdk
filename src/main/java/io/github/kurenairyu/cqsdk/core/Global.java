package io.github.kurenairyu.cqsdk.core;

import io.github.kurenairyu.cqsdk.pojo.Bot;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Kurenai
 * @since 2021-04-02 14:21
 */

@Getter
@Setter
@Slf4j
public class Global {

    private static final Map<String, Bot> BOT       = new HashMap<>();
    private static final Bot              EMPTY_BOT = new Bot();

    public static long id = 0;

    private Global() {

    }

    public static Bot getBot(ChannelHandlerContext ctx) {
        return getContextId(ctx)
                .map(BOT::get)
                .orElse(EMPTY_BOT);
    }

    public static void putBot(ChannelHandlerContext ctx, Bot bot) {
        getContextId(ctx).ifPresent(id -> putBot(id, bot));
    }

    public static void putBot(String idText, Bot bot) {
        if (bot != null && StringUtils.isNotBlank(idText)) {
            BOT.put(idText, bot);
            log.info("Bot({}) connected.", bot.getQq());
        }
    }

    public static void removeBot(ChannelHandlerContext ctx) {
        getContextId(ctx).ifPresent(Global::removeBot);
    }

    public static void removeBot(String idText) {
        log.info("Bot({}) disconnected.", BOT.remove(idText).getQq());
    }

    private static Optional<String> getContextId(ChannelHandlerContext ctx) {
        return Optional.ofNullable(ctx)
                .map(ChannelHandlerContext::channel)
                .map(Channel::id)
                .map(ChannelId::asLongText);
    }

}
