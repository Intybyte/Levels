package com.thexfactor117.levels.architectury.util;

import com.thexfactor117.levels.common.leveling.ItemType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;

public class ItemUtil {
    public static ItemType type(Item item) {
        if (item instanceof SwordItem) return ItemType.SWORD;
        if (item instanceof ArmorItem) return ItemType.ARMOR;
        if (item instanceof BowItem) return ItemType.BOW;
        if (item instanceof ShieldItem) return ItemType.SHIELD;
        return null;
    }
}
