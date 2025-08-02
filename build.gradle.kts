plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    kotlin("plugin.jpa") version "2.1.0"
    kotlin("plugin.allopen") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "gomushin"
version = "0.0.1-SNAPSHOT"

val mockkVersion = "1.13.10"
val kotestVersion = "5.5.4"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // spring boot
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // mysql
    runtimeOnly("com.mysql:mysql-connector-j")

    // h2
    runtimeOnly("com.h2database:h2")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

    // logging
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    // cache
    implementation("org.springframework.boot:spring-boot-starter-cache:3.3.10")
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.3.10")
    implementation("org.redisson:redisson:3.44.0")

    //security
    implementation("org.springframework.boot:spring-boot-starter-security")

    //jwt
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")

    // oauth2
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    // configuration processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // aws
    implementation("software.amazon.awssdk:s3:2.30.38")

    //okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //google auth
    implementation("com.google.auth:google-auth-library-oauth2-http:1.33.1")

    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    //serializable
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("io.mockk:mockk:${mockkVersion}")
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("org.springframework.stereotype.Component")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
