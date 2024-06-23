package com.croco.interview.management.order.config;

import com.croco.interview.management.order.model.response.PageableResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class HazelcastConfig extends CachingConfigurerSupport {

    private final ObjectMapper objectMapper;

    @Bean
    public CacheManager cacheManager() {
        ClientConfig config = new ClientConfig();
        config.getSerializationConfig().addSerializerConfig(new SerializerConfig()
                .setImplementation(new PageableOrderEntitySerializer(objectMapper))
                .setTypeClass(PageableResponse.class));
        config.getNetworkConfig()
                .setAddresses(List.of("bfa-hazelcast"));


        HazelcastInstance client = HazelcastClient.newHazelcastClient(config);
        return new HazelcastCacheManager(client);
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return null;
    }
}
