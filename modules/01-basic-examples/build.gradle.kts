plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    // Testcontainers
    implementation(platform("org.testcontainers:testcontainers-bom:1.19.3"))
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")

    // Junit dependencies.
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    // Optional Dependencies
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")


    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-core:1.4.12")
    implementation("ch.qos.logback:logback-classic:1.4.12")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")

}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}