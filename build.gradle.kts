import lt.neworld.gradle.jdeploy.JDeployExtension

plugins {
    application
    kotlin("jvm") version "1.3.10"
    id("lt.neworld.jdeploy") version "0.5.0"
}

group = "lt.neworld.logchopper"
version = "1.1.0"

repositories {
    mavenCentral()
    jcenter()
    maven("https://jitpack.io")
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.xenomachina:kotlin-argparser:${Versions.kotlinArgParser}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")
    implementation("com.github.ajalt:mordant:${Versions.mordant}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit5Version}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit5Version}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.junit5Version}")
    testImplementation("lt.neworld:kupiter:${Versions.kupiter}")
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform {

        }
        reports {
            html.isEnabled = true
        }
    }
}


application {
    mainClassName = "lt.neworld.logchopper.MainKt"
}

tasks.withType<Jar> {
    manifest.attributes.put("Main-Class", application.mainClassName)
    from(configurations.runtimeClasspath.map { if (it.isDirectory) it else zipTree(it) })
}

configure<JDeployExtension> {
    name = "gradle-logchopper"
    binName = "logchopper"
    author = "Andrius Semionovas"
    description = "CLI tool to split long gradle debug log by tasks"
    license = "Apache-2.0"
    repository = "https://github.com/neworld/logchopper"
}
