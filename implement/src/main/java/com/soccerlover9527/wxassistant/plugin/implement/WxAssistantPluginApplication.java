package com.soccerlover9527.wxassistant.plugin.implement;

import com.soccerlover9527.wxassistant.plugin.base.annotation.EnableWxAssistantPlugin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * entry point.
 * @author soccerlover9527@gmail.com
 */
@SpringBootApplication
@EnableWxAssistantPlugin
public class WxAssistantPluginApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxAssistantPluginApplication.class, args);
    }

}
