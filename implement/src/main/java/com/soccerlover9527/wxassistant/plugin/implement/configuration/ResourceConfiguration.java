package com.soccerlover9527.wxassistant.plugin.implement.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 * @author soccerlover9527@gmail.com
 */
@Configuration
@Getter
public class ResourceConfiguration {
    @Value("${wx-assistant-plugin.resources.path}")
    public String resourcePath;
}
