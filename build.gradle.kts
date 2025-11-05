plugins {
    kotlin("jvm") version PluginVersion.KOTLIN
    kotlin("plugin.spring") version PluginVersion.KOTLIN
    kotlin("plugin.jpa") version PluginVersion.KOTLIN
    id(Plugins.SPRING_BOOT) version PluginVersion.SPRING_BOOT
    id(Plugins.SPRING_DEPENDENCY_MANAGEMENT) version PluginVersion.SPRING_DEPENDENCY_MANAGEMENT
}

group = "team.exit-1.backend"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_21

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // Spring Boot Starters
    implementation(Dependencies.SPRING_BOOT_STARTER_WEB)
    implementation(Dependencies.SPRING_BOOT_STARTER_DATA_JPA)
    implementation(Dependencies.SPRING_BOOT_STARTER_VALIDATION)
    implementation(Dependencies.SPRING_BOOT_STARTER_SECURITY)
    implementation(Dependencies.SPRING_BOOT_STARTER_ACTUATOR)

    // Kotlin
    implementation(Dependencies.KOTLIN_REFLECT)

    // OpenFeign
    implementation(Dependencies.SPRING_CLOUD_STARTER_OPENFEIGN)
    implementation(Dependencies.OPEN_FEIGN_JACKSON)

    // SpringDoc OpenAPI
    implementation(Dependencies.SPRING_DOC_OPENAPI_SWAGGER_UI)

    // Database
    runtimeOnly(Dependencies.MYSQL_CONNECTOR)

    // Testing
    testImplementation(Dependencies.SPRING_BOOT_STARTER_TEST)
    testImplementation(Dependencies.KOTLIN_TEST_JUNIT5)
    testRuntimeOnly(Dependencies.JUNIT_PLATFORM_LAUNCHER)
    testImplementation(Dependencies.SPRING_SECURITY_TEST)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${DependencyVersion.SPRING_CLOUD}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jar {
    isEnabled = false
}

tasks.bootJar {
    isEnabled = true
}