package com.maemresen.testcontainers.workshop.shared.container.cases.global;

import org.junit.jupiter.api.Test;

class SharedContainerComparisonChild1Test extends AbstractBaseGlobalSharedContainerTest {
    @Test
    void child1SpecificTest() {
        assertNotNewContainerCreated();
    }
}