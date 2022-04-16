import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id ("org.jetbrains.kotlin.plugin.spring") version "1.6.10"
    id ("org.springframework.boot") version "2.6.3"
    id ("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "com.github.fastmirrorserver"
version = "unknown"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")

    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.springframework.boot:spring-boot-starter-jdbc:2.6.5")
    implementation("org.springframework.boot:spring-boot-starter:2.6.5")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.5")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")
    implementation("com.auth0:java-jwt:3.19.1")

    implementation("org.ktorm:ktorm-core:3.4.1")
    implementation("org.ktorm:ktorm-support-postgresql:3.4.1")
    implementation("org.ktorm:ktorm-jackson:3.4.1")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.5")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.processResources {
    val profile = System.getProperty("profile") ?: "pond"
    filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("profile" to profile))
}

tasks.bootJar {
    val version = System.getProperty("version") ?: "unknown"
    val suffix = System.getProperty("version_suffix") ?: "release"
    project.version = "${version}-${suffix}"
}

tasks.compileJava {
    inputs.files(tasks.processResources)
}