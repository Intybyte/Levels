package com.thexfactor117.levels.architectury.events.attributes;

import com.thexfactor117.levels.architectury.nbt.NBTHelper;
import com.thexfactor117.levels.architectury.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.nbt.INBT;
import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;

public class EventSoulBound {
    public static void register() {

        PlayerEvent.PLAYER_CLONE.register( (oldPlayer, newPlayer,wonGame) -> {
            //only if death
            if (wonGame) {
                return;
            }

            Inventory oldInv = oldPlayer.inventory;

            processInventory(newPlayer, oldInv.armor);
            processInventory(newPlayer, oldInv.items);
            processInventory(newPlayer, oldInv.offhand);


        });
    }

    private static void processInventory(ServerPlayer newPlayer, Iterable<ItemStack> oldInv) {
        for (Iterator<ItemStack> iterator = oldInv.iterator(); iterator.hasNext(); ) {
            ItemStack stack = iterator.next();

            ItemType type = ItemUtil.type(stack.getItem());
            if (type == null) {
                continue;
            }

            CompoundTag baseNbt = stack.getOrCreateTag();
            if (baseNbt == null) {
                continue;
            }

            INBT nbt = NBTHelper.toCommon(baseNbt);
            if (AnyAttributes.SOUL_BOUND.hasAttribute(nbt)) {
                iterator.remove();
                newPlayer.inventory.add(stack);
            }
        }
    }
}
