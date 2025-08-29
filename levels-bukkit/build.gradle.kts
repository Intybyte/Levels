import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    id("io.freefair.lombok") version "8.14.2"
    id("com.gradleup.shadow") version "9.1.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

val mainPackage = "com.thexfactor117.levels.bukkit"
group = mainPackage
version = "3.0.0"

bukkit {
    name = "Levels"
    main = "com.thexfactor117.levels.bukkit.LevelsPlugin"
    apiVersion = "1.20"
    version = project.version.toString()
    authors = listOf("Vaan1310", "TheXFactor117")

    commands {
        register("levels") {
            description = "Open the levels GUI with info about the held item"
            permission = "levels.base"
        }
    }

    permissions {
        register("levels.base") {
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

repositories {
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven("https://repo.xenondevs.xyz/releases")

}

val invuiVersion = "1.46"
val invuiGroup = "xyz.xenondevs.invui"

val invuiArtifacts = listOf(
    "invui-core",
    "inventory-access",
    "inventory-access-r18",
    "inventory-access-r19",
    "inventory-access-r20",
    "inventory-access-r21",
    "inventory-access-r22",
    "inventory-access-r23",
    "inventory-access-r24"
)

val inuviFinalList = invuiArtifacts.map { "$invuiGroup:$it:$invuiVersion" }

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    implementation(project(":levels-common"))
    implementation("com.github.cryptomorin:XSeries:13.3.3")

    inuviFinalList.forEach(::implementation)
}

val targetJavaVersion = 21

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
}

tasks.named<ShadowJar>("shadowJar") {
    // Relocate the class to your package
    relocate("com.cryptomorin.xseries", "$mainPackage.libs.xseries")
    relocate("xyz.xenondevs.invui", "$mainPackage.libs.invui")
    relocate("xyz.xenondevs.inventoryaccess", "$mainPackage.libs.inventoryaccess")

    dependencies {
        include(project(":levels-common"))
        include(dependency("com.github.cryptomorin:XSeries"))
        invuiArtifacts.forEach {
            include(dependency("$invuiGroup:$it"))
        }

        exclude("com/cryptomorin/xseries/XBiome*")
        exclude("com/cryptomorin/xseries/NMSExtras*")
        exclude("com/cryptomorin/xseries/NoteBlockMusic*")
        exclude("com/cryptomorin/xseries/SkullCacheListener*")
        exclude("com/cryptomorin/xseries/XBlock*")
        exclude("com/cryptomorin/xseries/XItemStack*")
        exclude("com/cryptomorin/xseries/XWorldBorder*")
        exclude("com/cryptomorin/xseries/particles/*")
        exclude("com/cryptomorin/xseries/profiles/**")
        exclude("com/cryptomorin/xseries/inventory/*")
    }

    // Optionally change the JAR name if desired
    archiveClassifier.set("all")
}

tasks.build {
    dependsOn("shadowJar")
}

