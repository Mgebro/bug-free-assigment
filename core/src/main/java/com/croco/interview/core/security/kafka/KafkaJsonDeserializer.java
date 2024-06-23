package com.croco.interview.core.security.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class KafkaJsonDeserializer implements Deserializer<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public Object deserialize(String topic, byte[] bytes) {
        Class type;
        switch (topic) {
            case UserRegistered.TOPIC -> type = UserRegistered.class;
            default -> throw new IllegalStateException("Topic: $topic is not configured!");
        }
        try {
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
