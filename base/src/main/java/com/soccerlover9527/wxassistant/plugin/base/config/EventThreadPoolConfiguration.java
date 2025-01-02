package com.soccerlover9527.wxassistant.plugin.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author soccerlover9527@gmail.com
 */
@Configuration
public class EventThreadPoolConfiguration {

    @Bean
    @ConditionalOnMissingBean(ExecutorService.class)
    public ExecutorService getThreadPoolExecutor() {
        return new ForkJoinPool();
    }


    @Bean("applicationEventMulticaster")
    public SimpleApplicationEventMulticaster asyncMulticaster(@Autowired ExecutorService executorService) {
        var simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        simpleApplicationEventMulticaster.setTaskExecutor(executorService);
        return simpleApplicationEventMulticaster;
    }
}
