package com.maemresen.testcontainers.workshop.shared.container.cases.global;

import org.junit.jupiter.api.Test;

class SharedContainerComparisonChild2Test extends AbstractBaseGlobalSharedContainerTest {

    @Test
    void child2SpecificTest() {
        assertNotNewContainerCreated();
    }
}
