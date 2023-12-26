package com.maemresen.testcontainers.workshop.jupiter.extension;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class JupiterExtensionTest {

    @Container
    GenericContainer<?> helloWorldContainer = new GenericContainer<>("kennethreitz/httpbin") ;

    @Test
    void isContainerUp() {
        assertNotNull(helloWorldContainer);
        assertTrue(helloWorldContainer.isRunning());
    }
}
