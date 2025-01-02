package com.soccerlover9527.wxassistant.plugin.base.example;

import com.soccerlover9527.wxassistant.plugin.base.IWxCommandHandler;
import com.soccerlover9527.wxassistant.plugin.base.annotation.WxAssistantPlugin;
import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;

import java.util.List;

/**
 * @author soccerlover9527@gmail.com
 */
@WxAssistantPlugin("ping")
public class PingHandler implements IWxCommandHandler {

    @Override
    public WxAssistantResponse onMessage(CommandMessage message) {
        return WxAssistantResponse.text(message.getGid(), "pong");
    }

    @Override
    public List<String> supportCommands() {
        return List.of("ping");
    }
}
