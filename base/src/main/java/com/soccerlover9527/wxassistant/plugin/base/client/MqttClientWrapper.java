package com.soccerlover9527.wxassistant.plugin.base.client;

import com.soccerlover9527.wxassistant.plugin.base.MessageDispatcher;
import com.soccerlover9527.wxassistant.plugin.base.MessageParser;
import com.soccerlover9527.wxassistant.plugin.base.config.MqttProperties;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.paho.client.mqttv3.*;

import java.util.Arrays;

/**
 * @author soccerlover9527@gmail.com
 */
@Slf4j
public class MqttClientWrapper implements IClientWrapper {
    private final MqttClient mqttClient;
    private final String clientId;
    private final MessageDispatcher inBoundMessageDispatcher;
    private final MqttConnectOptions mqttConnectOptions;
    private final MqttProperties.TopicAndQos[] topics;
    private final String[] replyTopics;

    public MqttClientWrapper(MqttProperties.Client clientProperties, MessageDispatcher inBoundMessageDispatcher) {
        //  TODO cache
        this.inBoundMessageDispatcher = inBoundMessageDispatcher;
        this.topics = clientProperties.getTopics();
        this.replyTopics = clientProperties.getReplyTopics();
        var clientId = clientProperties.getClientId();
        try {
            if (clientProperties.getRandomClientId()) {
                this.clientId = clientProperties.getUsername() + "_" + RandomStringUtils.insecure().nextAlphanumeric(8);
            } else {
                this.clientId = clientId;
            }
            this.mqttClient = new MqttClient(clientProperties.getUrls()[0], this.clientId);

            this.mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setServerURIs(clientProperties.getUrls());
            mqttConnectOptions.setUserName(clientProperties.getUsername());
            mqttConnectOptions.setPassword(clientProperties.getPassword().toCharArray());
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setAutomaticReconnect(true);
        } catch (MqttException e) {
            throw new RuntimeException("Failed to init MQTT client:" + e.getMessage(), e);
        }
    }

    @Override
    public String id() {
        return clientId;
    }

    @Override
    public void connect() {
        try {
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("Mqtt connection lost.", cause);//    盐须
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    //  TODO cache id in case of repeated message.
                    var message = MessageParser.parseMqttMessage(mqttMessage);
                    var commandMessage = message.toCommandMessage();
                    if (commandMessage != null) {
                        inBoundMessageDispatcher.dispatch(clientId, commandMessage);
                    }
                    inBoundMessageDispatcher.dispatch(clientId, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // do nothing
                }
            });
            mqttClient.connect(mqttConnectOptions);
            log.info("Connecting to MQTT client: {}, url: {} success.", clientId, Arrays.toString(mqttConnectOptions.getServerURIs()));
            String[] topic = Arrays.stream(topics).map(MqttProperties.TopicAndQos::getTopic).toArray(String[]::new);
            int[] qos = Arrays.stream(topics).mapToInt(MqttProperties.TopicAndQos::getQos).toArray();
            mqttClient.subscribe(topic, qos);
            log.info("Subscribe to MQTT client:{} topics: {} success.", clientId, Arrays.toString(topics));
        } catch (MqttException e) {
            throw new RuntimeException("Failed to connect to MQTT client:%s, url:%s".formatted(clientId, mqttConnectOptions.getServerURIs()[0]) + e);
        }
    }

    @Override
    public void sendResponse(WxAssistantResponse response) {
        byte[] body = MessageParser.parseWxAssistantMessage(response);
        for (String replyTopic : replyTopics) {
            try {
                mqttClient.publish(replyTopic, body, 1, false);
            } catch (MqttException e) {
                throw new RuntimeException("Failed to reply message to MQTT client.", e);
            }
        }
    }
}
