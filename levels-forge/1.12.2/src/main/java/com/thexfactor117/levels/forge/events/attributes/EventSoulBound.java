package com.thexfactor117.levels.forge.events.attributes;

import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.forge.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttribute;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventSoulBound {
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            EntityPlayer player = event.getEntityPlayer();
            player.inventory.copyInventory(event.getOriginal().inventory);
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Item item;

        for (int i = 0; i < event.getDrops().size(); i++) {
            item = event.getDrops().get(i).getItem().getItem();

            ItemType type = ItemType.of(item);
            if (type == null) {
                continue;
            }

            ItemStack stack = event.getDrops().get(i).getItem();
            NBTTagCompound baseNbt = NBTHelper.loadStackNBT(stack);
            if (baseNbt == null) {
                continue;
            }

            INBT nbt = NBTHelper.toCommon(baseNbt);
            if (WeaponAttribute.SOUL_BOUND.hasAttribute(nbt) || ArmorAttribute.SOUL_BOUND.hasAttribute(nbt) || BowAttribute.SOUL_BOUND.hasAttribute(nbt) || ShieldAttribute.SOUL_BOUND.hasAttribute(nbt)) {
                event.getDrops().remove(i);
                player.inventory.addItemStackToInventory(stack);
            }
        }
    }
}
