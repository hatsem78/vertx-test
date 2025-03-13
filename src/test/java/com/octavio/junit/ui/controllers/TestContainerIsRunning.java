package com.octavio.junit.ui.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
public class TestContainerIsRunning {

    @Container
    @ServiceConnection
    private static KafkaContainer kafkaContainer = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest"));

    @Test
    @DisplayName("The test container is created and is running")
    void testContainerIsRunning() {
        assertTrue(kafkaContainer.isCreated(), "kafka Container has not been created");
        assertTrue(kafkaContainer.isRunning(), "kafka Container is not running");
    }

}



