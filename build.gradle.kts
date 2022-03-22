import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id ("org.jetbrains.kotlin.plugin.spring") version "1.6.10"
    id ("org.springframework.boot") version "2.6.3"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.github.fastmirrorserver"
version = "2.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")

    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.6.4")
    implementation("org.springframework.boot:spring-boot-starter:2.6.4")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.4")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    implementation("org.ktorm:ktorm-core:3.4.1")
    implementation("org.ktorm:ktorm-support-postgresql:3.4.1")
    implementation("org.ktorm:ktorm-jackson:3.4.1")

    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-swagger2:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.4")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}