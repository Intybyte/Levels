package com.thexfactor117.levels.bukkit.events.attributes;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.thexfactor117.levels.bukkit.nbt.NBTHelper;
import com.thexfactor117.levels.bukkit.util.ItemUtil;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.nbt.INBT;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

/**
 *
 * @author TheXFactor117
 *
 */
public class EventSoulBound implements Listener {

    private final Multimap<UUID, ItemStack> keepItems = ArrayListMultimap.create();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Iterator<ItemStack> iter = event.getDrops().iterator();

        while (iter.hasNext()) {
            ItemStack item = iter.next();

            Material mat = item.getType();
            if (mat.isAir()) {
                continue;
            }

            ItemType type = ItemUtil.type(mat);
            if (type == null) {
                continue;
            }

            if (!item.hasItemMeta()) {
                continue;
            }

            INBT nbt = NBTHelper.toCommon(item.getItemMeta().getPersistentDataContainer());
            if (AnyAttributes.SOUL_BOUND.hasAttribute(nbt)) {
                iter.remove();
                keepItems.put(player.getUniqueId(), item.clone());
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        Collection<ItemStack> stacks = keepItems.get(id);
        stacks.forEach(player.getInventory()::addItem);
        keepItems.removeAll(id);
    }
}
