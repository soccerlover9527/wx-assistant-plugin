package com.soccerlover9527.wxassistant.plugin.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soccerlover9527.wxassistant.plugin.base.core.Command;
import com.soccerlover9527.wxassistant.plugin.base.core.WxAssistantResponse;
import com.soccerlover9527.wxassistant.plugin.base.core.message.Message;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

/**
 * @author soccerlover9527@gmail.com
 */
public class MessageParser {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Message parseMqttMessage(MqttMessage mqttMessage) {
        try {
            return objectMapper.readValue(mqttMessage.getPayload(), Message.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse mqtt msg cause:" + e.getMessage() + ", message:" + mqttMessage, e);
        }
    }

    public static byte[] parseWxAssistantMessage(WxAssistantResponse wxAssistantResponse) {
        try {
            Command<Object> command = new Command<>();
            command.setCommand("sendMessage");
            command.setParam(wxAssistantResponse);
            return objectMapper.writeValueAsBytes(command);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse wx assistant msg cause:" + e.getMessage() + ", message:" + wxAssistantResponse, e);
        }
    }
}
