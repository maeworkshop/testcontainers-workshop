# Testcontainers Workshop Repository

## Overview
Welcome to the Testcontainers Workshop Repository! This repository is dedicated to hands-on examples and exercises for learning and mastering Testcontainers, an advanced tool for running applications in isolated Docker containers.

## Prerequisites
Before you start, ensure you have the following installed:
- [JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven 3.8](https://maven.apache.org/download.cgi) (or higher)
- [Docker](https://www.docker.com/get-started) (for running Testcontainers)

## Setting Up
1. **Clone the Repository**: Clone this repository to your local machine using `git clone`.
2. **Configure Docker**: Ensure Docker is running on your system as Testcontainers will need it to create and manage containers.
3. **Install Dependencies**: Run `mvn install` to install the necessary dependencies.

## Workshop Modules
This workshop includes the following modules:
- **01-basic-examples**: Contains basic examples of using Testcontainers.
- **02-spring-boot-examples**: Contains examples for integrating Testcontainers with Spring Boot applications.

## Running the Examples
To run the examples in each module:
1. Navigate to the specific module directory (`01-basic-examples` or `02-spring-boot-examples`).
2. Execute the tests using `mvn test`.
