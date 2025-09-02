package com.thexfactor117.levels.architectury.forge;

import com.thexfactor117.levels.architectury.Levels;
import com.thexfactor117.levels.architectury.keybindings.Keybinds;
import com.thexfactor117.levels.common.config.Configs;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Levels.MOD_ID)
public final class LevelsForge {

    public LevelsForge() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Submit our event bus to let Architectury API register our content on the right time.
        eventBus.addListener(this::setup);
        eventBus.addListener(this::doClientStuff);
        EventBuses.registerModEventBus(Levels.MOD_ID, eventBus);

        // Run our common setup.
        Levels.init();
    }

    private void setup(final FMLCommonSetupEvent event) {

        // Init configs (assuming your own config system)
        Configs.init(FMLPaths.CONFIGDIR.get().resolve(Levels.MOD_ID).toFile());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Keybinds.register();
    }
}
