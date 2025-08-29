package com.thexfactor117.levels.bukkit.events;

import com.thexfactor117.levels.bukkit.util.ItemUtil;
import com.thexfactor117.levels.bukkit.util.WeaponHelper;
import com.thexfactor117.levels.common.config.Configs;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EventCreateWeapon implements Runnable {
    private static final Map<Material, ItemMeta> DEFAULT_META = new EnumMap<>(Material.class);

    @Override
    public void run() {
        Set<NamespacedKey> blackList = Configs.getInstance().main.getStringSet("itemBlackList")
                .stream().map(NamespacedKey::fromString)
                .collect(Collectors.toSet());

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (ItemStack stack : player.getInventory()) {
                if (stack == null) {
                    continue;
                }

                Material type = stack.getType();
                if (ItemUtil.type(type) == null) {
                    continue;
                }

                NamespacedKey nsKey = type.getKey();
                if (blackList.contains(nsKey)) {
                    continue;
                }

                if (stack.getItemMeta() == null) {
                    stack.setItemMeta(
                        DEFAULT_META.computeIfAbsent(
                                type, Bukkit.getItemFactory()::getItemMeta
                        )
                    );
                } else if (!stack.getItemMeta().getPersistentDataContainer().isEmpty()) {
                    continue; // don't modify other plugin stuff, might create problems otherwise
                }

                WeaponHelper.create(stack, player);
            }
        }
    }
}
