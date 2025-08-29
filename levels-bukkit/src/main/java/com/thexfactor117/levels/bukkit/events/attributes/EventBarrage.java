package com.thexfactor117.levels.bukkit.events.attributes;

import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventBarrage implements Listener {

    @EventHandler
    public void onBowFire(EntityShootBowEvent event) {
        Entity shooter = event.getEntity();
        if (!(shooter instanceof Player)) {
            return;
        }

        Player player = (Player) shooter;
        ItemStack stack = event.getBow();

        if (stack == null) {
            return;
        }

        INBT nbt = NBTHelper.toCommon(stack.getItemMeta().getPersistentDataContainer());
        if (!BowAttribute.BARRAGE.hasAttribute(nbt)) {
            return;
        }

        float force = event.getForce();
        for (int i = 0; i < (int) BowAttribute.BARRAGE.getCalculatedValue(nbt); i++) {
            Arrow arrow = player.getWorld().spawnArrow(player.getLocation(), player.getEyeLocation().toVector(), force * 3, 20f);
            arrow.setShooter(player);
            arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        }
    }
}
