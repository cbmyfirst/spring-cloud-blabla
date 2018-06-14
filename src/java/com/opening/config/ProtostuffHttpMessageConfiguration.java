/**
 * Copyright (C) Opening Information Technology, LTD. 
 * All Rights Reserved.
 *
 * ProtostuffHttpMessageConfiguration.java created on 12/6/2018 18:36:43 by Sandy Deng 
 */
package com.opening.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opening.http.ProtostuffHttpMessageConverter;

/**
 * <pre>
 * Protostuff启用入口
 * @author Sandy Deng
 * @date 12/6/2018 18:36:43
 *
 * </pre>
 */
@Configuration
@ConditionalOnClass({ProtostuffHttpMessageConverter.class})
@ConditionalOnProperty(name={"spring.http.converters.preferred-json-mapper"},
                       havingValue = "protostuff", 
                       matchIfMissing = true)
public class ProtostuffHttpMessageConfiguration
{
    @Bean
    @ConditionalOnMissingBean({ProtostuffHttpMessageConverter.class})
    public ProtostuffHttpMessageConverter protostuffHttpMessageConverter()
    {
        return new ProtostuffHttpMessageConverter();
    }
}
