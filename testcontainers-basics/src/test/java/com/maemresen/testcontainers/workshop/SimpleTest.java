package com.maemresen.testcontainers.workshop;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.*;


class SimpleTest {
    final GenericContainer<?> container = new GenericContainer<>("nginx");

    @Test
    void containerLifecycleTest(){
        container.start();
        assertTrue(container.isRunning());

        final String containerId = container.getContainerId();

        container.stop();
        assertFalse(container.isRunning());

        container.start();
        assertNotEquals(containerId, container.getContainerId());

        container.stop();
    }
}
