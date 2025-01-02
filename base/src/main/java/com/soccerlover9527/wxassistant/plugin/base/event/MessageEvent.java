package com.soccerlover9527.wxassistant.plugin.base.event;

import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;

/**
 * @author soccerlover9527@gmail.com
 */
public class MessageEvent extends BaseEvent {
    public MessageEvent(String clientName, Message message) {
        super(clientName, message);
    }
}
