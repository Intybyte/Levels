package com.thexfactor117.levels.forge.events;

import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.forge.nbt.NBTHelper;
import com.thexfactor117.levels.forge.util.ItemUtil;
import com.thexfactor117.levels.forge.util.WeaponHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.Set;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventCreateWeapon {
    @SubscribeEvent
    public void onCreateWeapon(PlayerTickEvent event) {
        if (event.player.getEntityWorld().isRemote) {
            return;
        }

        if (event.phase != Phase.START) {
            return;
        }

        for (ItemStack stack : event.player.inventory.mainInventory) {
            if (stack != null && ItemUtil.type(stack.getItem()) != null) {
                create(stack, event.player);
            }
        }
    }

    /**
     * Sets up a weapon with customized values.
     * @param stack
     */
    private void create(ItemStack stack, EntityPlayer player) {
        NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        if (nbt == null) {
            return;
        }


        Set<String> list = Configs.getInstance().main.getStringSet("itemBlackList");
        String nsKey = stack.getItem().getRegistryName().getNamespace();
        if (list.contains(nsKey)) return;

        WeaponHelper.create(stack, player);
    }
}
