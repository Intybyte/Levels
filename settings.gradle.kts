import org.gradle.kotlin.dsl.maven

rootProject.name = "Levels-Main"

include("levels-common")
include("levels-bukkit")


listOf(
    "1.16.5"
).forEach { mcVers ->
    listOf("forge", "fabric").forEach { platform ->
        val selected = "$platform-$mcVers"
        include("levels-$selected")
        project(":levels-$selected").apply {
            projectDir = file("./levels-$platform/$mcVers")
            buildFileName = "../../../multiversion/build.gradle.kts"
        }
    }
}

include("levels-forge-1.12.2")
project(":levels-forge-1.12.2").projectDir = file("./levels-forge/1.12.2")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://jenkins.usrv.eu:8081/nexus/content/repositories/releases/")
        maven {
            name = "OvermindDL1 Maven"
            url = uri("https://gregtech.overminddl1.com/")
        }
        maven {
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
        }
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.architectury.dev")
        maven("https://maven.fabricmc.net")
        maven("https://maven.minecraftforge.net")
        maven("https://repo.essential.gg/public")
        maven("https://repo.essential.gg/repository/maven-public")
    }

    // We also recommend specifying your desired version here if you're using more than one of the plugins,
    // so you do not have to change the version in multilpe places when updating.
    plugins {
        val egtVersion = "0.1.0" // should be whatever is displayed in above badge
        id("gg.essential.multi-version.root") version egtVersion
        id("gg.essential.multi-version.api-validation") version egtVersion
    }
}