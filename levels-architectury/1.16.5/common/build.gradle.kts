val project_mcVersion = "1.16.5"

architectury {
    minecraft = project_mcVersion
    common(rootProject.extra["enabled_platforms"].toString().split(','))
}

repositories {
    maven("https://maven.minecraftforge.net/")
    maven("https://files.minecraftforge.net/maven/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    mavenCentral()
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

loom {
    silentMojangMappingsLicense()
}

dependencies {
    // We depend on Fabric Loader here to use the Fabric @Environment annotations,
    // which get remapped to the correct annotations on each platform.
    // Do NOT use other classes from Fabric Loader.
    minecraft("net.minecraft:minecraft:$project_mcVersion")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${rootProject.extra["fabric_loader_version"]}")

    modImplementation("me.shedaniel:architectury:${rootProject.extra["architectury_api_version"]}")
}
