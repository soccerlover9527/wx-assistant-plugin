/* Copyright (c) 2025, TD SYNNEX Corporation. All rights reserved */
package com.soccerlover9527.wxassistant.plugin.implement.repo;

import lombok.Getter;

/**
 * <p></p>
 *
 * @author Coulson.He@tdsynnex.com
 * @since 2025-01-24
 */
@Getter
public enum TermTypeEnum {
    /**
     * 词条,包括自定义脏话
     */
    TERM(1),
    /**
     * 脏话拼音, 用于检测是否为脏话
     */
    DIRTY_WORD(2);
    private final int type;

    TermTypeEnum(int type) {
        this.type = type;
    }
}
