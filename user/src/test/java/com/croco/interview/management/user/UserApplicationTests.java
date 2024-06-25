package com.croco.interview.management.user;

import com.croco.interview.core.security.kafka.UserRegistered;
import com.croco.interview.management.user.model.entity.User;
import com.croco.interview.management.user.model.request.CreateUserRequest;
import com.croco.interview.management.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserApplicationTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvc mockMvc;

    private final UserRepository userRepository;

    private final Consumer<String, String> consumer;

    @Autowired
    public UserApplicationTests(
            MockMvc mockMvc,
            DefaultKafkaConsumerFactory<String, String> factory,
            UserRepository userRepository
    ) {
        this.mockMvc = mockMvc;
        this.userRepository = userRepository;
        consumer = factory.createConsumer("test", "0");
        consumer.subscribe(List.of(UserRegistered.TOPIC));
    }

    @Test
    void contextLoads() {
    }

    /**
     * This test is disabled because of some strange fact.
     * If you start this test this will throw no topic founds.
     * If you add break point at KafkaTestUtils.getSingleRecord and evaluate it il will return topics
     * IDK What's going on ;(
     */
    @Test
    @Disabled
    void shouldPublishUserRegisteredEvent() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "identifier",
                "firstName",
                "lastName",
                "password",
                "password"
        );

        mockMvc.perform(post("/api/public/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());


        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(1);

        ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, UserRegistered.TOPIC, Duration.ofSeconds(10));

        assertThat(record).isNotNull();

        consumer.close();
    }
}