package com.maemresen.testcontainers.workshop.shared.container.comparison;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@Testcontainers
public abstract class AbstractBaseSharedContainerComparisonTest {

    static final GenericContainer<?> GLOBAL_SHARED_CONTAINER = new GenericContainer<>("nginx:mainline-alpine3.18-slim");

    @Container
    static final GenericContainer<?> CLASS_BASED_SHARED_CONTAINER = new GenericContainer<>("nginx:mainline-alpine3.18-slim");

    @Container
    final GenericContainer<?> TEST_METHOD_BASED_CONTAINER = new GenericContainer<>("nginx:mainline-alpine3.18-slim");

    static final String INITIAL_GLOBAL_SHARED_CONTAINER_ID;

    static {
        GLOBAL_SHARED_CONTAINER.start();
        INITIAL_GLOBAL_SHARED_CONTAINER_ID = GLOBAL_SHARED_CONTAINER.getContainerId();

        log.info("Initial global shared container ID: " + INITIAL_GLOBAL_SHARED_CONTAINER_ID);
    }

    static final Map<Class<?>, String> CLASS_BASED_CONTAINER_ID_MAP = new HashMap<>();
    static final Map<Class<?>, Set<String>> TEST_BASED_CONTAINER_ID_MAP = new HashMap<>();

    @BeforeAll
    static void init(TestInfo testInfo) {
        log.info("Executing {} on {}",
                testInfo.getDisplayName(),
                testInfo.getTestClass().orElseThrow().getSimpleName());
    }

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        var testClass = testInfo.getTestClass().orElseThrow();

        // check if global shared container ID is not changed
        assertEquals(INITIAL_GLOBAL_SHARED_CONTAINER_ID, GLOBAL_SHARED_CONTAINER.getContainerId(), "Container ID should be same");

        // class based container ID is not changed
        var classBasedContainerId = CLASS_BASED_CONTAINER_ID_MAP.get(testClass);
        if (CLASS_BASED_CONTAINER_ID_MAP.containsKey(testClass)) {
            assertEquals(CLASS_BASED_CONTAINER_ID_MAP.get(testClass), classBasedContainerId, "Container ID should be same");
        } else {
            assertFalse(CLASS_BASED_CONTAINER_ID_MAP.containsValue(classBasedContainerId), "Container ID should be new");
            CLASS_BASED_CONTAINER_ID_MAP.put(testClass, CLASS_BASED_SHARED_CONTAINER.getContainerId());
        }

        // test method based container ID is new
        var methodBasedContainerId = TEST_METHOD_BASED_CONTAINER.getContainerId();
        if (TEST_BASED_CONTAINER_ID_MAP.containsKey(testClass)) {
            var testBasedContainerIds = TEST_BASED_CONTAINER_ID_MAP.get(testClass);
            assertFalse(testBasedContainerIds.contains(methodBasedContainerId), "Container ID should be new");
            testBasedContainerIds.add(methodBasedContainerId);
        } else {
            TEST_BASED_CONTAINER_ID_MAP.values().forEach(containerIds -> assertFalse(containerIds.contains(methodBasedContainerId), "Container ID should be new"));
            TEST_BASED_CONTAINER_ID_MAP.computeIfAbsent(testClass, k -> new HashSet<>()).add(methodBasedContainerId);
        }
    }

    @Test
    void test1() {
    }

    @AfterAll
    static void afterAll(TestInfo testInfo) {
        log.info("Finalizing {}", testInfo.getTestClass().orElseThrow().getSimpleName());
        printContainerHistory();
    }

    private static void printContainerHistory() {
        var logMessageBuilder = new StringBuilder("Container History:").append("\n")
                .append("\tGlobal shared container ID:").append(GLOBAL_SHARED_CONTAINER.getContainerId());


        logMessageBuilder.append("\n").append("\tClass based shared container ID:");
        CLASS_BASED_CONTAINER_ID_MAP.forEach((testClass, containerId) -> {
            logMessageBuilder.append("\n")
                    .append("\t\t")
                    .append(testClass.getSimpleName())
                    .append(" -> ")
                    .append(containerId);
        });

        logMessageBuilder.append("\n").append("\tTest method based container ID:");
        TEST_BASED_CONTAINER_ID_MAP.forEach((testClass, containerIds) -> {
            logMessageBuilder.append("\n")
                    .append("\t\t")
                    .append(testClass.getSimpleName())
                    .append(" -> ")
                    .append(containerIds);
        });

        log.info(logMessageBuilder.toString());
    }
}
