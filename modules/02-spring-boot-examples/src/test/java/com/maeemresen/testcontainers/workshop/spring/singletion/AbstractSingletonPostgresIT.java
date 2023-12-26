package com.maeemresen.testcontainers.workshop.spring.singletion;

import com.maeemresen.testcontainers.workshop.spring.util.ContainerHolder;
import com.maeemresen.testcontainers.workshop.spring.util.ContainerFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@SpringBootTest
public abstract class AbstractSingletonPostgresIT {

    protected static final ContainerHolder<PostgreSQLContainer<?>> GLOBAL_POSTGRESQL_CONTAINER
            = ContainerFactory.getIntegrationTestDbContainer("Singleton Postgres Container");

    static {
        GLOBAL_POSTGRESQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        final var container = GLOBAL_POSTGRESQL_CONTAINER.getContainer();
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @AfterEach
    void init(final TestInfo testInfo){
        log.info("Executed {} successfully.", testInfo.getDisplayName());
    }
}
