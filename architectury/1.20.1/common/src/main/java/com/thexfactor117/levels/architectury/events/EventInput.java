package com.thexfactor117.levels.architectury.events;

import com.thexfactor117.levels.architectury.gui.GuiItemInformation;
import com.thexfactor117.levels.architectury.keybindings.Keybinds;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientRawInputEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class EventInput {
    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientRawInputEvent.KEY_PRESSED.register((mc, keyCode, scanCode, action, modifiers) -> {
            Player player = mc.player;
            if (player == null || mc.screen != null) return EventResult.pass();

            ItemStack stack = player.getMainHandItem();
            if (stack == null) return EventResult.pass();

            ItemType type = ItemUtil.type(stack.getItem());
            if (type == null) return EventResult.pass();

            if (!Keybinds.OPEN_ATTRIBUTES.matches(keyCode, scanCode)) return EventResult.pass();

            mc.setScreen(new GuiItemInformation());
            return EventResult.interruptTrue();
        });
    }
}
