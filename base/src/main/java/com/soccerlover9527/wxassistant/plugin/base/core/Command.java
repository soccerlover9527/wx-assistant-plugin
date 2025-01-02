package com.soccerlover9527.wxassistant.plugin.base.core;

import lombok.Data;

@Data
public class Command<T> {
    private String command;
    /**
     * 这里用泛型是因为 sendMessage 是 {@link WxAssistantResponse}
     * 但是其他命令不一定接受这个参数.
     */
    private T param;
}
