package com.soccerlover9527.wxassistant.plugin.base.core.message.ext;

import lombok.Data;

@Data
public class Revoke {
    public String oldMsgID;
    public String replaceMsg;
}
