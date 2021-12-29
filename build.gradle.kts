import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    application
    `maven-publish`
}

group = "moe.kurenai.bot"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    api("org.jetbrains.kotlin", "kotlin-reflect")
    api("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")

    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-jackson:2.9.0")
    api("com.squareup.okhttp3:logging-interceptor:4.9.3")
    api("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    api("com.fasterxml.jackson.core:jackson-core:2.13.1")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    api("io.netty:netty-all:4.1.72.Final")

    //logging
    api("ch.qos.logback:logback-core:1.3.0-alpha10")
    api("ch.qos.logback:logback-classic:1.3.0-alpha10")
    api("org.apache.logging.log4j:log4j-to-slf4j:2.16.0")
    api("org.slf4j:slf4j-api:2.0.0-alpha5")
    api("io.github.microutils", "kotlin-logging-jvm", "2.0.6")

    api("org.msgpack", "msgpack-core", "0.9.0")
    api("org.msgpack", "jackson-dataformat-msgpack", "0.9.0")
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