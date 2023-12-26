package com.maemresen.testcontainers.workshop.quiz;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
abstract class BaseTest {
    static final GenericContainer<?> container1 = new GenericContainer<>("nginx");
    static final GenericContainer<?> container2 = new GenericContainer<>("nginx");

    static {
        container1.start();
    }

    @Container
    static final GenericContainer<?> container3 = new GenericContainer<>("nginx");

    @Container
    final GenericContainer<?> container4 = new GenericContainer<>("nginx");

    @BeforeAll
    static void init() {
        container3.dependsOn(container1);
        container3.dependsOn(container2);
    }

    @Test
    void test1() {}
}
