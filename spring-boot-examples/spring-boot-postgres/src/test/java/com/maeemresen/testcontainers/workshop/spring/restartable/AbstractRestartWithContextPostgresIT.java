package com.maeemresen.testcontainers.workshop.spring.restartable;

import com.maeemresen.testcontainers.workshop.spring.util.ContainerFactory;
import com.maeemresen.testcontainers.workshop.spring.util.ContainerHolder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@ContextConfiguration(initializers = AbstractRestartWithContextPostgresIT.ContextInitializer.class)
@SpringBootTest
public abstract class AbstractRestartWithContextPostgresIT {

    public static class ContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {
        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
            try {
                GLOBAL_POSTGRESQL_CONTAINER.restart();
            } catch (Exception e) {
                log.error("Error while restarting the PostgreSQL container", e);
            }
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }

    protected static final ContainerHolder<PostgreSQLContainer<?>> GLOBAL_POSTGRESQL_CONTAINER
            = ContainerFactory.getIntegrationTestDbContainer("RestartWithContext Postgres Container");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        final var container = GLOBAL_POSTGRESQL_CONTAINER.getContainer();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @AfterEach
    void init(final TestInfo testInfo) {
        log.info("Executed {} successfully.", testInfo.getDisplayName());
    }
}
