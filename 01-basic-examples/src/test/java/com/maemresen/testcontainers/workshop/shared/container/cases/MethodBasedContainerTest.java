package com.maemresen.testcontainers.workshop.shared.container.cases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Emre Şen (maemresen@yazilim.vip), 22/01/2023
 */
@Testcontainers
class MethodBasedContainerTest {

    private static final Set<String> CREATED_CONTAINER_IDS = new HashSet<>();

    @Container
    final GenericContainer<?> CONTAINER = new GenericContainer<>("nginx:mainline-alpine3.18-slim");

    @AfterAll
    static void afterAll() {
        System.out.println("Created container IDs: " + CREATED_CONTAINER_IDS);
    }

    @Test
    void test_dummyTest1() {
        assertNewContainerCreated(CONTAINER);
    }

    @Test
    void test_dummyTest2() {
        assertNewContainerCreated(CONTAINER);
    }

    private void assertNewContainerCreated(GenericContainer<?> container) {
        String containerId = container.getContainerId();

        assertFalse(CREATED_CONTAINER_IDS.contains(containerId), "Container ID should not be created before");
        CREATED_CONTAINER_IDS.add(containerId);
    }
}
