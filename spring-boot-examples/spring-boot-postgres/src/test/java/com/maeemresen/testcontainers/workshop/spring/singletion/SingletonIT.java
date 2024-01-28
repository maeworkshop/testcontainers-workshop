package com.maeemresen.testcontainers.workshop.spring.singletion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.DirtiesContext;

@Slf4j
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class SingletonIT {

    @BeforeEach
    void init(final TestInfo testInfo) {
        log.info("TEST:{} is using {}",
                testInfo.getDisplayName(),
                AbstractSingletonPostgresIT.GLOBAL_POSTGRESQL_CONTAINER.toString());
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Suit1 extends AbstractSingletonPostgresIT {

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @DisplayName("Singleton Suit1 - Test1")
        @Order(1)
        void singleton1Test1() {
        }


        @Test
        @DisplayName("Singleton Suit1 - Test2")
        @Order(2)
        void singleton1Test2() {
        }
    }

    @Nested
    @Order(2)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class Suit2 extends AbstractSingletonPostgresIT {
        @Test
        @DisplayName("Singleton Suit2 - Test1")
        @Order(1)
        void singleton2Test1() {

        }

        @Test
        @DisplayName("Singleton Suit2 - Test2")
        @Order(2)
        void singleton2Test2() {

        }
    }
}
