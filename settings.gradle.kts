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

listOf("1.16.5").forEach { version ->
    val arcProj = "architectury-$version"
    val arcFolder = "architectury/$version"

    include(":$arcProj")
    project(":$arcProj").projectDir = file(arcFolder)

    include(":$arcProj:common")
    include(":$arcProj:fabric")
    include(":$arcProj:forge")

    project(":$arcProj:common").projectDir = file("$arcFolder/common")
    project(":$arcProj:fabric").projectDir = file("$arcFolder/fabric")
    project(":$arcProj:forge").projectDir = file("$arcFolder/forge")
}