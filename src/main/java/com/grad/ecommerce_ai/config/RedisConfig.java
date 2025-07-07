package com.grad.ecommerce_ai.config;

import com.grad.ecommerce_ai.entity.ActiveIngredient;
import com.grad.ecommerce_ai.entity.Category;
import com.grad.ecommerce_ai.entity.InventoryDrug;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, Category> categoryRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Category> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Category.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, ActiveIngredient> activeIngredientRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, ActiveIngredient> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ActiveIngredient.class));
        return template;
    }

    @Bean
    public RedisTemplate<String, InventoryDrug> inventoryDrugRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, InventoryDrug> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // Use String serializer for keys
        template.setKeySerializer(new StringRedisSerializer());

        // Use Jackson serializer for values
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Bean
    public RedisTemplate<String, String> resetTokenRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
