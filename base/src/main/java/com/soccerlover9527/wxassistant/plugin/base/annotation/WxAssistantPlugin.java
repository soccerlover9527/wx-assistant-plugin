package com.soccerlover9527.wxassistant.plugin.base.annotation;

import java.lang.annotation.*;

/**
 * @author soccerlover9527@gmail.com
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WxAssistantPlugin {
    String value() default "";
}
