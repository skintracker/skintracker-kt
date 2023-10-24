plugins {
    kotlin("jvm") version "1.9.0"
    id("org.graalvm.buildtools.native") version "0.9.28"
    application
}

group = "io.skintracker"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    // kotlinx-html dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-html:0.9.1")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.9.1")

    // ActiveJ dependencies
    implementation("io.activej:activej-common:5.5")
    implementation("io.activej:activej-http:5.5")
    implementation("io.activej:activej-inject:5.5")
    implementation("io.activej:activej-launcher:5.5")
    implementation("io.activej:activej-launchers-http:5.5")
    implementation("io.activej:activej-promise:5.5")

    // Log4j dependencies
    implementation("org.apache.logging.log4j:log4j-api:2.21.0")
    implementation("org.apache.logging.log4j:log4j-core:2.21.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.21.0")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(20)
}

graalvmNative {
    toolchainDetection.set(true)
    binaries {
        named("main") {
            imageName.set("skintracker")
            mainClass.set("io.skintracker.MainKt")
            buildArgs.add("-O4")
        }
        named("test") {
            buildArgs.add("-O0")
        }
    }
    binaries.all {
        buildArgs.add("--verbose")
    }
}

application {
    mainClass.set("io.skintracker.MainKt")
}