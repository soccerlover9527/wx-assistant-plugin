package com.soccerlover9527.wxassistant.plugin.base.event;

import com.soccerlover9527.wxassistant.plugin.base.core.message.BaseMessage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author soccerlover9527@gmail.com
 */
@Getter
public class BaseEvent extends ApplicationEvent {
    private final String clientName;

    public BaseEvent(String clientName, BaseMessage message) {
        super(message);
        this.clientName = clientName;
    }
}
