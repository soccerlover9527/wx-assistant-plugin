package com.soccerlover9527.wxassistant.plugin.base.annotation;

import com.soccerlover9527.wxassistant.plugin.base.config.MqttConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author soccerlover9527@gmail.com
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MqttConfiguration.class)
@ComponentScan("com.soccerlover9527.wxassistant.plugin.base")
public @interface EnableWxAssistantPlugin {
}
