package com.maeemresen.testcontainers.workshop.spring.util;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ContainerHolder<T extends GenericContainer<?>> {

    private final String name;

    @Getter
    private final T container;

    public void restart() {
        stop();
        start();
    }

    public void start() {
        log.info("{} is starting", name);
        container.start();
        log.info("{} started with instance {}", name, container.getContainerId());
    }

    public void stop() {
        if (container.isCreated() || container.isRunning()) {
            log.info("{} has running instance with id {}. Stopping it.",
                    name,
                    container.getContainerId());
            final var containerId = container.getContainerId();
            container.stop();
            log.info("{} instance stopped.", containerId);
        } else {
            log.info("{} has not any running instance to stop.", name);
        }
    }

    @Override
    public String toString() {
        final String containerId = Optional.ofNullable(container)
                .filter(ContainerState::isRunning)
                .map(GenericContainer::getContainerId)
                .orElse("non-exists");
        return String.format("Container[%s]=%s", name, containerId);
    }
}