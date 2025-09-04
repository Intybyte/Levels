package com.thexfactor117.levels.architectury.fabric.client;

import com.thexfactor117.levels.architectury.keybindings.Keybinds;
import net.fabricmc.api.ClientModInitializer;

public final class LevelsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Keybinds.register();
    }
}
