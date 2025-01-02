package com.soccerlover9527.wxassistant.plugin.base.core.message;

import lombok.Data;

/**
 * @author laowu
 */
@Data
public class BaseMessage {
    public String msgID;
    public int msgType;
    public String gid;
    public String groupName;
    public String uid;
    public String username;
    public long time;
}
