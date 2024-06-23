package com.croco.interview.core.security.kafka;

public record UserRegistered(
        String identifier
) {
    public static final String TOPIC = "user.registered";
}