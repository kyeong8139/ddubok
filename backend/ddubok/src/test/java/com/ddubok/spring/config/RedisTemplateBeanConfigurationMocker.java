package com.ddubok.spring.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Configuration
public class RedisTemplateBeanConfigurationMocker {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return mock(LettuceConnectionFactory.class);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        @SuppressWarnings("unchecked")
        RedisTemplate<String, Object> template = mock(RedisTemplate.class);
        when(template.getConnectionFactory()).thenReturn(redisConnectionFactory);
        return template;
    }

    // String, String 타입의 RedisTemplate 추가
    @Bean
    public RedisTemplate<String, String> stringRedisTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        @SuppressWarnings("unchecked")
        RedisTemplate<String, String> template = mock(RedisTemplate.class);
        when(template.getConnectionFactory()).thenReturn(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListener(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = mock(RedisMessageListenerContainer.class);
        when(container.getConnectionFactory()).thenReturn(connectionFactory);
        return container;
    }
}