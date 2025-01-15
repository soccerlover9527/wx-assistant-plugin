package com.soccerlover9527.wxassistant.plugin.base.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerlover9527.wxassistant.plugin.base.IWxMessageHandler;
import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author soccerlover9527@gmail.com
 */
@WxAssistantPlugin("message-print")
@Slf4j
@Component
public class MessagePrintHandler implements IWxMessageHandler {
    private final ObjectMapper objectMapper;

    public MessagePrintHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public WxAssistantResponse onMessage(Message message) {
        log.info("receive message: {}", objectMapper.writeValueAsString(message));
        return null;
    }
}
