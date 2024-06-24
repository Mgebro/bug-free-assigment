package com.croco.interview.management.user.service;

import com.croco.interview.core.security.kafka.UserRegistered;
import com.croco.interview.management.user.model.entity.User;
import com.croco.interview.management.user.model.request.CreateUserRequest;
import com.croco.interview.management.user.model.request.UpdateUserRequest;
import com.croco.interview.management.user.model.response.PageableResponse;
import com.croco.interview.management.user.model.response.UserResponse;
import com.croco.interview.management.user.model.response.UsersResponse;
import com.croco.interview.management.user.repository.UserRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, UserRegistered> kafkaTemplate;

    private <T> void updateField(Consumer<T> setter, @Nullable T value) {
        if (setter != null && value != null) {
            setter.accept(value);
        }
    }

    public void createUser(CreateUserRequest request) {
        final User user = User.builder()
                .withIdentifier(request.identifier())
                .withFirstName(request.firstName())
                .withLastName(request.lastName())
                .withPassword(passwordEncoder.encode(request.password()))
                .build();
        userRepository.save(user);

        UserRegistered event = new UserRegistered(user.getIdentifier());

        CompletableFuture<SendResult<String, UserRegistered>> resultCompletableFuture = kafkaTemplate.send(UserRegistered.TOPIC, event);

        resultCompletableFuture.handleAsync((result, exception) -> {
            if (result != null) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.debug("Publish finished for more details see: %s, exception: %s".formatted(metadata, exception));
            } else {
                log.debug("Publish finished with exception: %s".formatted(exception));
            }
            return null;
        });
    }

    public PageableResponse<UsersResponse> getUsers(Optional<Integer> page, Optional<Integer> size) {
        final Page<User> result = userRepository.findAll(PageRequest.of(page.orElse(0), size.orElse(20)));

        return new PageableResponse<>(
                result.getPageable().getPageNumber(),
                result.getPageable().getPageSize(),
                result.getTotalElements(),
                result.getContent()
                        .stream()
                        .map(user -> UsersResponse.builder()
                                .withId(user.getId())
                                .withIdentifier(user.getIdentifier())
                                .withRole(user.getRole())
                                .withFirstName(user.getFirstName())
                                .withLastName(user.getLastName())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    public UserResponse getUser(Long id) {
        final User user = Objects.requireNonNull(userRepository.findUserById(id), "User with id [%s] not found!".formatted(id));
        return UserResponse.builder()
                .withIdentifier(user.getIdentifier())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .build();
    }

    public UserResponse getMe(String identifier) {
        final User user = Objects.requireNonNull(userRepository.findUserByIdentifier(identifier), "User with identifier [%s] not found!".formatted(identifier));
        return UserResponse.builder()
                .withIdentifier(user.getIdentifier())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .build();
    }

    public void updateUser(Long id, UpdateUserRequest updateUserRequest) {
        final User user = Objects.requireNonNull(userRepository.findUserById(id), "User with id [%s] not found!".formatted(id));
        updateField(user::setIdentifier, updateUserRequest.identifier());
        updateField(user::setFirstName, updateUserRequest.firstName());
        updateField(user::setLastName, updateUserRequest.lastName());
        updateField(user::setRole, updateUserRequest.role());
        userRepository.save(user);
    }

    public void updateMe(String identifier, UpdateUserRequest updateUserRequest) {
        final User user = Objects.requireNonNull(userRepository.findUserByIdentifier(identifier), "User with identifier [%s] not found!".formatted(identifier));
        updateField(user::setIdentifier, updateUserRequest.identifier());
        updateField(user::setFirstName, updateUserRequest.firstName());
        updateField(user::setLastName, updateUserRequest.lastName());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        final User user = Objects.requireNonNull(userRepository.findUserById(id), "User with id [%s] not found!".formatted(id));
        userRepository.delete(user);
    }
}
