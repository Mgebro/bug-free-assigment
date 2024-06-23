package com.croco.interview.management.user.config;

import com.croco.interview.core.security.kafka.UserRegistered;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    NewTopic systemClientRegisteredTopic() {
        return new NewTopic(UserRegistered.TOPIC, 1, (short) 1);
    }
}
