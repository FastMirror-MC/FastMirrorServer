import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "1.8.10"
    id ("org.jetbrains.kotlin.plugin.spring") version "1.8.10"
    id ("org.springframework.boot") version "2.6.3"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.github.fastmirrorserver"
version = "0.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")

    implementation("org.postgresql:postgresql:42.3.8")

    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis:2.7.2")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")

    implementation("org.ktorm:ktorm-core:3.5.0")
    implementation("org.ktorm:ktorm-support-postgresql:3.5.0")
    implementation("org.ktorm:ktorm-jackson:3.5.0")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.bootJar {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "describe", "--tags")
        standardOutput = stdout
    }
    val output = stdout.toString("UTF-8")
    project.version = output.substring(1)
}