package com.thexfactor117.levels.architectury.keybindings;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.architectury.registry.KeyBindings;
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
        KeyBindings.registerKeyBinding(OPEN_ATTRIBUTES);
    }

}
