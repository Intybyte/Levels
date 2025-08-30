plugins {
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.3-SNAPSHOT"
}

architectury {
    common.set(rootProject.extra["enabled_platforms"].toString().split(','))
}

repositories {
    maven("https://maven.minecraftforge.net/")
    maven("https://files.minecraftforge.net/maven/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    mavenCentral()
}

dependencies {
    // We depend on Fabric Loader here to use the Fabric @Environment annotations,
    // which get remapped to the correct annotations on each platform.
    // Do NOT use other classes from Fabric Loader.
    modImplementation("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")

    modImplementation("dev.architectury:architectury:${rootProject.extra["architectury_api_version"]}")
}
