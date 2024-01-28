package com.maeemresen.testcontainers.workshop.spring.restartable;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.DirtiesContext;

@Slf4j
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class RestartableIT {

    @BeforeEach
    void init(final TestInfo testInfo) {
        log.info("TEST: {} is using {}",
                testInfo.getDisplayName(),
                AbstractRestartWithContextPostgresIT.GLOBAL_POSTGRESQL_CONTAINER.toString());
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Suit1 extends AbstractRestartWithContextPostgresIT {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Restartable Suit1 - Test1")
        @Order(1)
        void restartable1Test1() {
        }


        @Test
        @DisplayName("Restartable Suit1 - Test2")
        @Order(2)
        void restartable1Test2() {
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Suit2 extends AbstractRestartWithContextPostgresIT {
        @Test
        @DisplayName("Restartable Suit2 - Test1")
        @Order(1)
        void restartable2Test1() {

        }

        @Test
        @DisplayName("Restartable Suit2 - Test2")
        @Order(2)
        void restartable2Test2() {

        }
    }
}
