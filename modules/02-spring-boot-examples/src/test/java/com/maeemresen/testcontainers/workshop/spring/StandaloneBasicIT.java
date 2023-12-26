package com.maeemresen.testcontainers.workshop.spring;

import com.maeemresen.testcontainers.workshop.spring.domain.Person;
import com.maeemresen.testcontainers.workshop.spring.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StandaloneBasicIT {

    @Container
    static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER = new PostgreSQLContainer<>("postgres:15.1");

    static {
        POSTGRESQL_CONTAINER.withDatabaseName("integration-tests-db");
        POSTGRESQL_CONTAINER.withUsername("sa");
        POSTGRESQL_CONTAINER.withPassword("sa");
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void init(final TestInfo testInfo) {
        final String containerId = Optional.of(POSTGRESQL_CONTAINER)
                .filter(ContainerState::isRunning)
                .map(GenericContainer::getContainerId)
                .orElse("non-exists");
        final String containerInfo = String.format("Container[%s]=%s", "PostgreSQL Container", containerId);
        log.info("TEST:{} is using {}", testInfo.getDisplayName(), containerInfo);
    }

    private Person savePerson(String name) {
        var person = new Person();
        person.setName(name);
        return personRepository.save(person);
    }

    private String randomName() {
        return "name-" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("Standalone Test1")
    @Order(1)
    void whenFindByNameIgnoreCase_thenReturnPerson() {
        String name = randomName();
        Person savedPerson = savePerson(name);
        var findPerson = personRepository.findTopByNameIgnoreCase(name.toUpperCase());

        assertTrue(findPerson.isPresent(), "Person is not found");
        assertEquals(savedPerson.getId(), findPerson.get().getId());
    }

    @Test
    @DisplayName("Standalone Test2")
    @Order(2)
    void whenFindByNameNonExistingName_thenReturnEmpty() {
        String name = randomName();
        String nonExistingName = "non-existing-name";

        savePerson(name);
        var findPerson = personRepository.findTopByNameIgnoreCase(nonExistingName);

        assertTrue(findPerson.isEmpty(), "Person is found");
    }
}
