package com.soccerlover9527.wxassistant.plugin.base;

import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.CommandMessage;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import com.soccerlover9527.wxassistant.plugin.base.event.CommandEvent;
import com.soccerlover9527.wxassistant.plugin.base.event.MessageEvent;
import com.soccerlover9527.wxassistant.plugin.base.event.ResponseEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author soccerlover9527@gmail.com
 */
@Component
public class MessageDispatcher implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void dispatch(String clientName, CommandMessage message) {
        applicationContext.publishEvent(new CommandEvent(clientName, message));
    }

    public void dispatch(String clientName, Message message) {
        applicationContext.publishEvent(new MessageEvent(clientName, message));
    }

    public void dispatch(String clientName, WxAssistantResponse response) {
        applicationContext.publishEvent(new ResponseEvent(clientName, response));
    }
}
