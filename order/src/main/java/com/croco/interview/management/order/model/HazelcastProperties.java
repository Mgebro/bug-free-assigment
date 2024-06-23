package com.croco.interview.management.order.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.hazelcast")
public record HazelcastProperties(
        String server
) {
}