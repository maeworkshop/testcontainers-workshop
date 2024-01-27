package com.maeemresen.testcontainers.workshop.spring.util;

import lombok.experimental.UtilityClass;
import org.testcontainers.containers.PostgreSQLContainer;

@UtilityClass
public class ContainerFactory {
    public static <T extends PostgreSQLContainer<T>> ContainerHolder<PostgreSQLContainer<?>> getIntegrationTestDbContainer(final String name) {
        try (var container = new PostgreSQLContainer<T>("postgres:13.3")) {
            container.withDatabaseName("integration-tests-db");
            container.withUsername("sa");
            container.withPassword("sa");
            return ContainerHolder
                    .<PostgreSQLContainer<?>>builder()
                    .name(name)
                    .container(container)
                    .build();
        }
    }

}
