package com.thexfactor117.levels.bukkit.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

import static java.lang.invoke.MethodType.methodType;

public class StackUtil {
    private static final MethodHandle ItemStack$getItemName;
    private static final MethodHandle ItemStack$hasItemName;
    private static final boolean hasItemName;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        MethodHandle itemStack$getItemName1;
        try {
            itemStack$getItemName1 = lookup.findVirtual(ItemStack.class, "getItemName", methodType(String.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            itemStack$getItemName1 = null;
        }

        ItemStack$getItemName = itemStack$getItemName1;

        MethodHandle itemStack$hasItemName1;
        try {
            itemStack$hasItemName1 = lookup.findVirtual(ItemStack.class, "hasItemName", methodType(boolean.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            itemStack$hasItemName1 = null;
        }

        ItemStack$hasItemName = itemStack$hasItemName1;

        hasItemName = ItemStack$hasItemName != null && ItemStack$getItemName != null;
    }

    public static void safeRepair(ItemMeta meta, int repairDurability) {
        if (!(meta instanceof Damageable)) {
            return;
        }

        Damageable dmg = (Damageable) meta;
        dmg.setDamage(
                Math.max(dmg.getDamage() - repairDurability, 0)
        );
    }

    public static String getName(ItemStack item) {
        String displayName;
        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName() && meta.getDisplayName() != null) {
            displayName = meta.getDisplayName();
        } else if (hasItemName) {
            try {

                if ((boolean) ItemStack$hasItemName.invoke(item)) {
                    displayName = ItemStack$getItemName.invoke(item).toString();
                } else {
                    displayName = processDisplayName(item, meta);
                }

            } catch (Throwable ignored) {
                displayName = processDisplayName(item, meta);
            }
        } else {
            displayName = processDisplayName(item, meta);
        }

        return displayName;
    }

    private static String processDisplayName(ItemStack item, ItemMeta meta) {
        String displayName;
        if (!meta.hasDisplayName()) {
            displayName = getFriendlyName(item);
        } else {
            displayName = "";
        }
        return displayName;
    }

    private static String getFriendlyName(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return "Air";

        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }

        return capitalizeFully(itemStack.getType().name().replace("_", " ").toLowerCase());
    }

    private static String capitalizeFully(String name) {
        if (name == null) {
            return "";
        }

        if (name.length() <= 1) {
            return name.toUpperCase();
        }

        if (!name.contains("_")) {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        }

        StringBuilder sbName = new StringBuilder();
        for (String subName : name.split("_"))
            sbName.append(subName.substring(0, 1).toUpperCase() + subName.substring(1).toLowerCase()).append(" ");

        return sbName.substring(0, sbName.length() - 1);
    }
}
