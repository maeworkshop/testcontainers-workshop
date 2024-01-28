package com.maemresen.testcontainers.workshop.shared.container.cases.global;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractBaseGlobalSharedContainerTest {

    static final GenericContainer<?> GLOBAL_SHARED_CONTAINER = new GenericContainer<>("nginx:mainline-alpine3.18-slim");

    static final String INITIAL_GLOBAL_SHARED_CONTAINER_ID;

    static {
        GLOBAL_SHARED_CONTAINER.start();
        INITIAL_GLOBAL_SHARED_CONTAINER_ID = GLOBAL_SHARED_CONTAINER.getContainerId();
    }

    protected static void assertNotNewContainerCreated() {
        assertEquals(INITIAL_GLOBAL_SHARED_CONTAINER_ID, GLOBAL_SHARED_CONTAINER.getContainerId(), "Container ID should be same");
    }
}
