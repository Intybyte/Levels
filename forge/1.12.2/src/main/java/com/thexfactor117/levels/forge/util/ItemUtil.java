package com.thexfactor117.levels.forge.util;

import com.thexfactor117.levels.common.leveling.ItemType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;

public class ItemUtil {
    public static ItemType type(Item item) {
        if (item instanceof ItemSword) return ItemType.SWORD;
        if (item instanceof ItemArmor) return ItemType.ARMOR;
        if (item instanceof ItemBow) return ItemType.BOW;
        if (item instanceof ItemShield) return ItemType.SHIELD;
        return null;
    }
}
