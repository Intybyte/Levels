package com.thexfactor117.levels.architectury.forge;

import com.thexfactor117.levels.architectury.Levels;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Levels.MOD_ID)
public final class LevelsForge {
    public LevelsForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(Levels.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        Levels.init();
    }
}
