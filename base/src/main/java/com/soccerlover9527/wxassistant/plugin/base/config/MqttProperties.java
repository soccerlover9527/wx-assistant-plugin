package com.soccerlover9527.wxassistant.plugin.base.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author soccerlover9527@gmail.com
 */
@Data
@ConfigurationProperties(prefix = "wx-assistant-plugin.mqtt")
public class MqttProperties {
    private List<Client> clients;

    @Data
    public static class Client {
        String clientId;
        String username;
        String password;
        /**
         * server address with port
         */
        String[] urls;
        TopicAndQos[] topics;
        String[] replyTopics;
        Boolean cleanSession = true;
        Boolean randomClientId;
        /**
         * register plugins to this client.
         */
        String[] plugins;
    }

    @Data
    public static class TopicAndQos {
        private String topic;
        private int qos = 0;
    }
}
