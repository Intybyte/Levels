package com.thexfactor117.levels.architectury.fabric;

import com.thexfactor117.levels.architectury.Levels;
import net.fabricmc.api.ModInitializer;

public final class LevelsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        Levels.init();
    }
}
