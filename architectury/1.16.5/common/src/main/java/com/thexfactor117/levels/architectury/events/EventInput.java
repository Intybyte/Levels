package com.thexfactor117.levels.architectury.events;

import com.thexfactor117.levels.architectury.gui.GuiItemInformation;
import com.thexfactor117.levels.architectury.keybindings.Keybinds;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import me.shedaniel.architectury.event.events.client.ClientRawInputEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.InteractionResult.PASS;
import static net.minecraft.world.InteractionResult.SUCCESS;

public class EventInput {
    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientRawInputEvent.KEY_PRESSED.register((mc, keyCode, scanCode, action, modifiers) -> {
            Player player = mc.player;
            if (player == null || mc.screen != null) return PASS;

            ItemStack stack = player.getMainHandItem();
            if (stack == null) return PASS;

            ItemType type = ItemUtil.type(stack.getItem());
            if (type == null) return PASS;

            if (!Keybinds.OPEN_ATTRIBUTES.matches(keyCode, scanCode)) return PASS;

            mc.setScreen(new GuiItemInformation());
            return SUCCESS;
        });
    }
}
