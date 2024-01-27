package com.maemresen.testcontainers.workshop.jupiter.extension.inheratance;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class BaseJupiterExtensionInheratanceTest {
    @Container
    GenericContainer<?> container = new GenericContainer<>("nginx");
}
