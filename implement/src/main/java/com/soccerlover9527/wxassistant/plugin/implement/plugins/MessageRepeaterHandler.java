package com.soccerlover9527.wxassistant.plugin.implement.plugins;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.soccerlover9527.wxassistant.plugin.base.IWxMessageHandler;
import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
@Setter
@WxAssistantPlugin("repeater")
public class MessageRepeaterHandler implements IWxMessageHandler {
    //  Map<groupId, Cache<msgUid, msgCounter>>
    Map<String, Cache<String, MessageRecordHolder>> groupCache = new ConcurrentHashMap<>();
    private Long repeatSelfDelay = 2 * 60 * 1000L;

    @Override
    public WxAssistantResponse onMessage(Message message) {
        var gid = message.getGid();
        var cache = groupCache.computeIfAbsent(gid, (_ignore) -> initCache());

        var content = message.getContent();
        //  skip blank message.
        if (StringUtils.isBlank(content.trim())) {
            return null;
        }
        //  hash msg
        var msgUId = DigestUtils.sha256Hex(content);
        var holder = cache.getIfPresent(msgUId);
        if (holder == null) {
            synchronized (cache) {
                holder = new MessageRecordHolder(new AtomicLong(System.currentTimeMillis()), new AtomicInteger(1));
                cache.put(msgUId, holder);
            }
        } else {
            int messageCount = holder.counter().incrementAndGet();
            //  1. only repeat message when message count is 2 or 2 minutes later.
            //  2. just repeat once.
            if (messageCount == 2 || System.currentTimeMillis() - holder.timestamp.get() > repeatSelfDelay) {
                holder.timestamp.set(System.currentTimeMillis());
                return WxAssistantResponse.text(message.getGid(), content);
            }
        }
        return null;
    }

    record MessageRecordHolder(AtomicLong timestamp, AtomicInteger counter) {
    }

    private static Cache<String, MessageRecordHolder> initCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(3))
                .maximumSize(100)
                .build();
    }
}
