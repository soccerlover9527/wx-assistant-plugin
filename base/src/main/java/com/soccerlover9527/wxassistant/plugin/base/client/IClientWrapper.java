package com.soccerlover9527.wxassistant.plugin.base.client;

import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;

/**
 * @author soccerlover9527@gmail.com
 */
public interface IClientWrapper {
    void connect();

    String id();

    /**
     * 发送回复
     *
     * @param response response
     */
    void sendResponse(WxAssistantResponse response);
}
