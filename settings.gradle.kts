import org.gradle.kotlin.dsl.maven

rootProject.name = "Levels-Main"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://jenkins.usrv.eu:8081/nexus/content/repositories/releases/")
        maven {
            name = "OvermindDL1 Maven"
            url = uri("https://gregtech.overminddl1.com/")
            metadataSources {
                mavenPom()
                gradleMetadata()
                artifact()
            }
        }

        maven {
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
        }

        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://files.minecraftforge.net/maven/")
    }
}

include("common")

include("forge-1.12.2")
project(":forge-1.12.2").projectDir = file("./forge/1.12.2")

include("bukkit")

val arc1165 = "architectury-1.16.5"
val arcFolder1165 = "architectury/1.16.5"

include(":$arc1165")
project(":$arc1165").projectDir = file(arcFolder1165)

include(":$arc1165:common")
include(":$arc1165:fabric")
include(":$arc1165:forge")

project(":$arc1165:common").projectDir = file("$arcFolder1165/common")
project(":$arc1165:fabric").projectDir = file("$arcFolder1165/fabric")
project(":$arc1165:forge").projectDir = file("$arcFolder1165/forge")