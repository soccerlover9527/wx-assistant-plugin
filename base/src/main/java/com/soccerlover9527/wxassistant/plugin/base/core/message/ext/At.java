package com.soccerlover9527.wxassistant.plugin.base.core.message.ext;

import lombok.Data;

@Data
public class At {
    private String uid;
    private String name;
    private boolean bot;
    private int offset;
    private int length;
}
