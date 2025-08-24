import org.gradle.kotlin.dsl.maven

rootProject.name = "Levels-Main"
include("levels-forge-1.12.2")
project(":levels-forge-1.12.2").projectDir = file("./levels-forge/1.12.2")

include("levels-common")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://jenkins.usrv.eu:8081/nexus/content/repositories/releases/")
        maven {
            name = "OvermindDL1 Maven"
            url = uri("https://gregtech.overminddl1.com/")
        }
        maven {
            name = "GTNH Maven"
            url = uri("https://nexus.gtnewhorizons.com/repository/public/")
        }
    }
}