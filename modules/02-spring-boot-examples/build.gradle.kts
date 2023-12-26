plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
}

extensions.configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_21 // Replace with your desired version
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {

    // Spring Boot Deps
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Testcontainers deps
    testImplementation("org.junit.jupiter:junit-jupiter-params")

    // Other (Optional)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("io.rest-assured:rest-assured")
}
