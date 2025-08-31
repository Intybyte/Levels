import org.jetbrains.gradle.ext.IdeaCompilerConfiguration
import org.jetbrains.gradle.ext.RunConfigurationContainer

plugins {
    id("java")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
    id("gg.essential.multi-version.root")
}

preprocess {
    // Here you first need to create a node per version you support and assign it an integer Minecraft version.
    // The mappings value is currently meaningless.
    val fabric11605 = createNode("levels-fabric-1.16.5", 11605, "yarn")
    val forge11605 = createNode("levels-forge-1.16.5", 11605, "mcp")

    // And then you need to tell the preprocessor which versions it should directly convert between.
    // This should form a directed graph with no cycles (i.e. a tree), which the preprocessor will then traverse to
    // produce source code for all versions from the main version.
    // Do note that the preprocessor can only convert between two projects when they are either on the same Minecraft
    // version (but use different mappings, e.g. 1.16.2 forge to fabric), or when they are using the same intermediary
    // mappings (but on different Minecraft versions, e.g. 1.12.2 forge to 1.8.9 forge, or 1.16.2 fabric to 1.18 fabric)
    // but not both at the same time, i.e. you cannot go straight from 1.12.2 forge to 1.16.2 fabric, you need to go via
    // an intermediary 1.16.2 forge project which has something in common with both.
    fabric11605.link(forge11605)
    // For any link, you can optionally specify a file containing extra mappings which the preprocessor cannot infer by
    // itself, e.g. forge intermediary names do not contain class names, so you may need to supply mappings for those
    // manually.
}


group = "com.thexfactor117.levels"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}