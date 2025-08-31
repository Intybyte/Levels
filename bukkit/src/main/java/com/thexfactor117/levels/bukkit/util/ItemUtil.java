package com.thexfactor117.levels.bukkit.util;

import com.thexfactor117.levels.common.leveling.ItemType;
import org.bukkit.Material;

public class ItemUtil {
    public static boolean isArmor(Material material) {
        return isHelmet(material) || isChestplate(material) || isLeggings(material) || isBoots(material);
    }

    public static boolean isHelmet(Material material) {
        return material.toString().contains("HELMET");
    }

    public static boolean isChestplate(Material material) {
        return material.toString().contains("CHESTPLATE");
    }

    public static boolean isLeggings(Material material) {
        return material.toString().contains("LEGGINGS");
    }

    public static boolean isBoots(Material material) {
        return material.toString().contains("BOOTS");
    }

    public static boolean isWeapon(Material material) {
        String materialName = material.toString();
        return isSword(material) || materialName.contains("_AXE");
    }

    public static boolean isSword(Material material) {
        String materialName = material.toString();
        return materialName.contains("SWORD");
    }

    public static ItemType type(Material material) {
        if (material == Material.BOW) return ItemType.BOW;
        if (material == Material.SHIELD) return ItemType.SHIELD;
        if (isSword(material)) return ItemType.SWORD;
        if (isArmor(material)) return ItemType.ARMOR;

        return null;
    }
}
