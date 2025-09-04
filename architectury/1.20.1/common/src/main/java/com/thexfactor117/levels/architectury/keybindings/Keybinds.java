package com.thexfactor117.levels.architectury.keybindings;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
    public static final KeyMapping OPEN_ATTRIBUTES = new KeyMapping(
        "key.gui.weapon_interface",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_L,
        "key.levels"
    );

    public static void register() {
        KeyMappingRegistry.register(OPEN_ATTRIBUTES);
    }

}
