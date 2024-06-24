package com.croco.interview.management.order;

import com.github.dockerjava.api.model.ExposedPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
                .withReuse(true);
        kafkaContainer.setPortBindings(List.of("9092:9092"));
        return kafkaContainer;
    }

    @Bean
    GenericContainer<?> hazelcastContainer() {
        try (
                GenericContainer<?> genericContainer = new GenericContainer<>(DockerImageName.parse("hazelcast/hazelcast:5.4.0"))
        ) {
            genericContainer.withReuse(true);
            genericContainer.addExposedPort(5701);
            genericContainer.start();
            genericContainer.setPortBindings(List.of("5701:5701"));
            genericContainer.getExposedPorts();
            return genericContainer;
        }
    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
        postgreSQLContainer.withReuse(true);
        postgreSQLContainer.setPortBindings(List.of("5432:5432"));
        return postgreSQLContainer;
    }

}
