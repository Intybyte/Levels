package com.thexfactor117.levels.events;

import com.thexfactor117.levels.config.Config;
import com.thexfactor117.levels.util.NBTHelper;
import com.thexfactor117.levels.util.WeaponHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * 
 * @author TheXFactor117
 *
 */
public class EventCreateWeapon 
{
	@SubscribeEvent
	public void onCreateWeapon(PlayerTickEvent event)
	{
        if (event.player.getEntityWorld().isRemote) {
            return;
        }

        if (event.phase != Phase.START) {
            return;
        }

        for (ItemStack stack : event.player.inventory.mainInventory)
        {
            if (stack != null && (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemShield))
            {
                create(stack, event.player);
            }
        }
    }
	
	/**
	 * Sets up a weapon with customized values.
	 * @param stack
	 */
	private void create(ItemStack stack, EntityPlayer player)
	{
		NBTTagCompound nbt = NBTHelper.loadStackNBT(stack);

        if (nbt == null) {
            return;
        }


        String nsKey = stack.getItem().getRegistryName().getNamespace();
        for (int j = 0; j < Config.itemBlacklist.length; j++)
        {
            if (Config.itemBlacklist[j].equals(nsKey))
            {
                return;
            }
        }

        WeaponHelper.create(stack, player);
    }
}
