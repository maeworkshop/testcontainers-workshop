package com.maemresen.testcontainers.workshop;


import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Testcontainers
class NetworkingTest {

    // Constants for container images, expected responses, aliases, and ports
    private static final String NGINX_IMAGE = "nginx:1.17.10-alpine";
    private static final String CURL_IMAGE = "curlimages/curl:8.2.1";
    private static final String EXPECTED_RESPONSE = "{name: Emre, surname: Sen}";
    private static final String NGINX_NETWORK_ALIAS = "app-nginx";
    private static final int NGINX_NETWORK_PORT = 8080;

    // Network instance to ensure both containers can communicate
    private static final Network NETWORK = Network.newNetwork();

    // Container definitions
    @Container
    static final GenericContainer<?> nginxContainer = new GenericContainer<>(NGINX_IMAGE);

    @Container
    static final GenericContainer<?> curlContainer = new GenericContainer<>(CURL_IMAGE);

    static {
        log.info("Configuring containers...");
        configureNginxContainer();
        configureCurlContainer();
        log.info("Containers configured.");
    }

    private static void configureNginxContainer() {
        final var nginxHttpRequestListenerCmd = """
                while true;
                do
                    printf 'HTTP/1.1 200 OK\\n\\n%s' | nc -l -p %d;
                done
            """.formatted(EXPECTED_RESPONSE, NGINX_NETWORK_PORT);

        nginxContainer.withNetwork(NETWORK)
                .withNetworkAliases(NGINX_NETWORK_ALIAS)
                .withCommand("/bin/sh", "-c", nginxHttpRequestListenerCmd);

        log.info("Nginx container configured to listen on port {} and return response: {}", NGINX_NETWORK_PORT, EXPECTED_RESPONSE);
    }

    private static void configureCurlContainer() {
        curlContainer.dependsOn(nginxContainer)
                .withNetwork(NETWORK)
                .withCommand("sleep", "infinity");

        log.info("Curl container configured to depend on Nginx container.");
    }

    @Test
    void expectResponseFromNginx() throws IOException, InterruptedException {
        log.info("Executing test: expectResponseFromNginx...");

        final var response = fetchResponse();

        assertEquals(EXPECTED_RESPONSE, response, "Response must be " + EXPECTED_RESPONSE);
        log.info("Test passed. Expected response received.");
    }

    private String fetchResponse() throws IOException, InterruptedException {
        final var url = String.format("http://%s:%s", NGINX_NETWORK_ALIAS, NGINX_NETWORK_PORT);
        log.info("Fetching response from URL: {}", url);

        final var execResult = curlContainer.execInContainer("curl", url);
        final var response = execResult.getStdout();

        log.info("Response received: {}", response);
        return response;
    }
}
