import lt.neworld.gradle.jdeploy.JDeployExtension

buildscript {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.0-rc-57")
    }
}

plugins {
    application
    id("lt.neworld.jdeploy") version "0.4.0"
}

apply {
    plugin("kotlin")
}

group = "lt.neworld.logchopper"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlinx")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.0-rc-57")
    implementation("com.xenomachina:kotlin-argparser:${Versions.kotlinArgParser}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit5Version}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit5Version}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${Versions.junit5Version}")
    testImplementation("lt.neworld:kupiter:${Versions.kupiter}")
}

tasks {
    "test"(Test::class) {
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
    author = "Andrius Semionovas"
    description = "CLI tool to split long gradle debug log by tasks"
    license = "Apache-2.0"
    repository = "https://github.com/neworld/logchopper"
}
