plugins {
    id("java")
    id("io.freefair.lombok") version "8.14.2"
}

group = "com.thexfactor117.levels.common"
version = rootProject.property("version") as String

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    // Generate sources and javadocs jars when building and publishing
    withSourcesJar()
    withJavadocJar()
}

tasks.test {
    useJUnitPlatform()
}