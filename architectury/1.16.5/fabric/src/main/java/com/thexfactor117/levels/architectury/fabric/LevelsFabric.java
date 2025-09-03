package com.thexfactor117.levels.architectury.fabric;

import com.thexfactor117.levels.architectury.Levels;
import com.thexfactor117.levels.common.config.Configs;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public final class LevelsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        Configs.init(
            FabricLoader.getInstance().getConfigDir().toFile()
        );

        // Run our common setup.
        Levels.init();
    }
}
