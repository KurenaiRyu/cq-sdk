import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    application
    `maven-publish`
}

group = "moe.kurenai.cq"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

object Versions {
    const val jackson = "2.13.1"
    const val log4j = "2.17.1"
    const val disruptor = "3.4.4"
    const val ktor = "2.0.2"
}

dependencies {
    api("org.jetbrains.kotlin", "kotlin-reflect")
    api("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")

    api("com.fasterxml.jackson.core:jackson-annotations:${Versions.jackson}")
    api("com.fasterxml.jackson.core:jackson-core:${Versions.jackson}")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.jackson}")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}")

    implementation("io.ktor:ktor-client-core:${Versions.ktor}")
    implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
    implementation("io.ktor:ktor-client-encoding:${Versions.ktor}")
    implementation("io.ktor:ktor-client-websockets:${Versions.ktor}")
    implementation("io.ktor:ktor-client-auth:${Versions.ktor}")
    implementation("io.ktor:ktor-server-core:${Versions.ktor}")
    implementation("io.ktor:ktor-server-cio:${Versions.ktor}")
    implementation("io.ktor:ktor-server-websockets:${Versions.ktor}")
    implementation("io.ktor:ktor-serialization-jackson:${Versions.ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}")

    implementation("com.github.dblock:oshi-core:3.4.0")

    api("io.netty:netty-all:4.1.77.Final")

    //logging
    api("org.apache.logging.log4j:log4j-core:${Versions.log4j}")
    api("org.apache.logging.log4j:log4j-api:${Versions.log4j}")

    api("com.lmax:disruptor:${Versions.disruptor}")

    testApi(kotlin("test"))
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
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/KurenaiRyu/cq-sdk")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
                }
            }
        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
}