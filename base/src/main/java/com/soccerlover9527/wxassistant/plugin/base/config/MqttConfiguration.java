package com.soccerlover9527.wxassistant.plugin.base.config;

import com.soccerlover9527.wxassistant.plugin.base.HandlerContext;
import com.soccerlover9527.wxassistant.plugin.base.MessageDispatcher;
import com.soccerlover9527.wxassistant.plugin.base.client.IClientWrapper;
import com.soccerlover9527.wxassistant.plugin.base.client.MqttClientWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @author soccerlover9527@gmail.com
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfiguration implements ApplicationContextAware {
    private final MqttProperties mqttProperties;
    private final MessageDispatcher inBoundMessageDispatcher;
    private final HandlerContext handlerContext;

    public MqttConfiguration(MqttProperties mqttProperties, MessageDispatcher inBoundMessageDispatcher, HandlerContext handlerContext) {
        this.mqttProperties = mqttProperties;
        this.inBoundMessageDispatcher = inBoundMessageDispatcher;
        this.handlerContext = handlerContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        var defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        mqttProperties.getClients().forEach(clientConfig -> {
            //  create client
            IClientWrapper mqttClientWrapper = new MqttClientWrapper(clientConfig, inBoundMessageDispatcher);
            //  register plugin to client.
            String[] plugins = clientConfig.getPlugins();
            handlerContext.registerClientHandlers(clientConfig.getClientId(), plugins);
            //  connect.
            mqttClientWrapper.connect();
            //  register to spring context.
            defaultListableBeanFactory.registerSingleton(mqttClientWrapper.id(), mqttClientWrapper);
        });
    }
}
