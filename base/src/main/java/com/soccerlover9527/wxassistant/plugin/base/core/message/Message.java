package com.soccerlover9527.wxassistant.plugin.base.core.message;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soccerlover9527.wxassistant.plugin.base.core.message.ext.At;
import com.soccerlover9527.wxassistant.plugin.base.core.message.ext.Media;
import com.soccerlover9527.wxassistant.plugin.base.core.message.ext.Quote;
import com.soccerlover9527.wxassistant.plugin.base.core.message.ext.Revoke;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


@Getter
@Setter
@ToString
public class Message extends BaseMessage {
    private String content;
    private Quote quote;
    private Revoke revoke;
    private At at;
    private Media media;
    private String event;
    private ObjectNode data;

    public String getContent() {
        return content == null ? "" : content;
    }

    public CommandMessage toCommandMessage() {
        if (!getContent().startsWith("#")) {
            return null;
        }
        if ("#".equals(getContent().trim())) {
            return null;
        }
        CommandMessage msg = new CommandMessage();
        msg.setMsgID(getMsgID());
        msg.setMsgType(getMsgType());
        msg.setGid(getGid());
        msg.setGroupName(getGroupName());
        msg.setUid(getUid());
        msg.setUsername(getUsername());
        msg.setRawMessage(getContent());
        String[] commands = StringUtils.split(getContent().trim().substring(1), " ", 2);
        msg.setCommand(commands[0]);
        msg.setParameter("");
        if (commands.length > 1) {
            msg.setParameter(commands[1]);
        }
        return msg;
    }
}
