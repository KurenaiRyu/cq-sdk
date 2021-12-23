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
    implementation("org.jetbrains.kotlin", "kotlin-reflect")
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

    implementation("io.netty:netty-all:4.1.72.Final")

    //logging
    implementation("ch.qos.logback:logback-core:1.3.0-alpha10")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha10")
    implementation("org.apache.logging.log4j:log4j-to-slf4j:2.16.0")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha5")
    implementation("io.github.microutils", "kotlin-logging-jvm", "2.0.6")

    implementation("org.msgpack", "msgpack-core", "0.9.0")
    implementation("org.msgpack", "jackson-dataformat-msgpack", "0.9.0")
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

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Main-Class" to main,
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            )
        )
    }

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}