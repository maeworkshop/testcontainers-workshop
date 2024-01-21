pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

rootProject.name = "testcontainers-workshop"

include(":modules:01-basic-examples")
include(":modules:02-spring-boot-examples:basics")
include(":modules:02-spring-boot-examples:spring-cloud-stream-kafka")