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
}

apply {
    plugin("kotlin")
}

group = "lt.neworld.gradle"
version = "0.0.1"

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
    mainClassName = "lt.neworld.gradle.logchopper.MainKt"
}

tasks.withType<Jar> {
    manifest.attributes.put("Main-Class", application.mainClassName)
    from(configurations.compile.map { if (it.isDirectory) it else zipTree(it) })
}
