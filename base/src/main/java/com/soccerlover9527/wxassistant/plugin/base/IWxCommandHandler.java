package com.soccerlover9527.wxassistant.plugin.base;

import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;

import java.util.List;

/**
 * @author soccerlover9527@gmail.com
 */
public interface IWxCommandHandler {
    /**
     * handle command message.
     *
     * @param message msg
     */
    WxAssistantResponse onMessage(CommandMessage message);

    /**
     * list support command.
     *
     * @return list of command str.
     */
    List<String> supportCommands();
}
