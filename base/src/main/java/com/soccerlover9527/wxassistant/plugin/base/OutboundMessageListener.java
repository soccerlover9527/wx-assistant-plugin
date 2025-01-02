package com.soccerlover9527.wxassistant.plugin.base;

import com.soccerlover9527.wxassistant.plugin.base.client.IClientWrapper;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.event.ResponseEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
public class OutboundMessageListener implements ApplicationListener<ResponseEvent> {
    private final ApplicationContext applicationContext;

    public OutboundMessageListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ResponseEvent event) {
        IClientWrapper client = applicationContext.getBean(event.getClientId(), IClientWrapper.class);
        client.sendResponse((WxAssistantResponse) event.getSource());
    }
}
