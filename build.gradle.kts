import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.61"
    application
}

group = "lt.neworld.gradle"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
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
    mainClassName = "lt.neworld.gradle.logparser.MainKt"
}

tasks.withType<Jar> {
    manifest.attributes.put("Main-Class", application.mainClassName)
    from(configurations.compile.map { if (it.isDirectory) it else zipTree(it) })
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}
