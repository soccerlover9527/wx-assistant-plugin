package com.soccerlover9527.wxassistant.plugin.base.event;

import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;

/**
 * @author soccerlover9527@gmail.com
 */
public class CommandEvent extends BaseEvent {

    public CommandEvent(String clientName, CommandMessage message) {
        super(clientName, message);
    }
}
