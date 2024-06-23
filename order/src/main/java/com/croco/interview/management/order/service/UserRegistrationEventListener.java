package com.croco.interview.management.order.service;

import com.croco.interview.core.security.kafka.UserRegistered;
import com.croco.interview.management.order.model.entity.User;
import com.croco.interview.management.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationEventListener {

    private final UserRepository userRepository;

    @KafkaListener(
            id = "user-created-listener",
            topics = UserRegistered.TOPIC,
            containerFactory = "kafkaListenerContainerFactory"
    )
    void userRegistered(UserRegistered event) {
        User user = userRepository.findUserByIdentifier(event.identifier());
        if (user == null) {
            user = User.builder()
                    .withIdentifier(event.identifier())
                    .build();
            userRepository.save(user);
        }
    }
}
