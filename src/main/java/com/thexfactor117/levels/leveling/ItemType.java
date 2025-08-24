package com.thexfactor117.levels.leveling;

import com.thexfactor117.levels.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.leveling.attributes.WeaponAttribute;
import com.thexfactor117.levels.leveling.attributes.components.AttributeBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum ItemType {
    SWORD,
    ARMOR,
    BOW,
    SHIELD;

    public static final Map<ItemType, List<? extends AttributeBase>> ATTRIBUTE_MAPPER = new EnumMap<>(ItemType.class);

    static {
        ATTRIBUTE_MAPPER.put(SWORD, WeaponAttribute.WEAPON_ATTRIBUTES);
        ATTRIBUTE_MAPPER.put(ARMOR, ArmorAttribute.ARMOR_ATTRIBUTES);
        ATTRIBUTE_MAPPER.put(BOW, BowAttribute.BOW_ATTRIBUTES);
        ATTRIBUTE_MAPPER.put(SHIELD, ShieldAttribute.SHIELD_ATTRIBUTES);
    }

    public static ItemType of(Item item) {
        if (item instanceof ItemSword) return SWORD;
        if (item instanceof ItemArmor) return ARMOR;
        if (item instanceof ItemBow) return BOW;
        if (item instanceof ItemShield) return SHIELD;
        return null;
    }

    public List<? extends AttributeBase> list() {
        return ATTRIBUTE_MAPPER.get(this);
    }
}
