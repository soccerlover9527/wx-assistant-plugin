package com.soccerlover9527.wxassistant.plugin.base.event;

import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * @author soccerlover9527@gmail.com
 */
@Getter
public class ResponseEvent extends ApplicationEvent {
    private final String clientId;

    public ResponseEvent(String clientId, WxAssistantResponse response) {
        super(response);
        this.clientId = clientId;
    }
}
