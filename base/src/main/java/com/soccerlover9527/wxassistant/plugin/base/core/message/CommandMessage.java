package com.soccerlover9527.wxassistant.plugin.base.core.message;

import lombok.*;

@Getter
@Setter
@ToString
public class CommandMessage extends BaseMessage {
    private String rawMessage;
    private String command;
    private String parameter;
}
