/* Copyright (c) 2025, TD SYNNEX Corporation. All rights reserved */
package com.soccerlover9527.wxassistant.plugin.base.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p></p>
 *
 * @author soccerlover9527@gmail.com
 * @since 2025-01-15
 */
@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
