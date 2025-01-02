package com.soccerlover9527.wxassistant.plugin.base;

import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;

/**
 * @author soccerlover9527@gmail.com
 */
public interface IWxMessageHandler {
    /**
     * handle message.
     *
     * @param message message
     */
    WxAssistantResponse onMessage(Message message);
}
