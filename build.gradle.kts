import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
    `maven-publish`
}

group = "moe.kurenai.cq"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

object Versions {
    const val jackson = "2.13.1"
    const val log4j = "2.17.1"
    const val disruptor = "3.4.4"
}

dependencies {
    api("org.jetbrains.kotlin", "kotlin-reflect")
    api("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")

    api("com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}")
    api("com.fasterxml.jackson.core:jackson-core:${Versions.jackson}")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.jackson}")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}")

    api("io.netty:netty-all:4.1.72.Final")

    //logging
    api("org.apache.logging.log4j:log4j-core:${Versions.log4j}")
    api("org.apache.logging.log4j:log4j-api:${Versions.log4j}")

    api("com.lmax:disruptor:${Versions.disruptor}")
}

val main = "MainKt"

application {
    mainClass.set("MainKt")
}
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            // groupId = project.group
            // artifactId = project.name
            // version = project.version
            from(components["java"])
        }
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}