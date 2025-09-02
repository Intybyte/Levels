package com.thexfactor117.levels.architectury.events;

import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.architectury.util.WeaponHelper;
import com.thexfactor117.levels.common.config.Configs;
import me.shedaniel.architectury.event.events.TickEvent;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class EventCreateWeapon {
    public static void register() {
        TickEvent.PLAYER_PRE.register((p) -> {
            if (p.level.isClientSide) {
                return;
            }

            Set<String> set = Configs.getInstance().main.getStringSet("itemBlackList");

            for (ItemStack stack : p.inventory.items) {
                String nsKey = Registry.ITEM.getKey(stack.getItem()).toString();

                if (set.contains(nsKey)) continue;

                if (ItemUtil.type(stack.getItem()) != null) {
                    WeaponHelper.create(stack, p);
                }
            }
        });
    }
}
