package com.thexfactor117.levels.leveling;

import com.thexfactor117.levels.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.leveling.attributes.WeaponAttribute;
import com.thexfactor117.levels.leveling.attributes.components.AttributeBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum ItemType {
    SWORD("levels.attributes.weapons.info"),
    ARMOR("levels.attributes.armors.info"),
    BOW("levels.attributes.bows.info"),
    SHIELD("levels.attributes.shields.info");

    public static final Map<ItemType, List<? extends AttributeBase>> ATTRIBUTE_MAPPER = new EnumMap<>(ItemType.class);

    static {
        ATTRIBUTE_MAPPER.put(SWORD, WeaponAttribute.WEAPON_ATTRIBUTES);
        ATTRIBUTE_MAPPER.put(ARMOR, ArmorAttribute.ARMOR_ATTRIBUTES);
        ATTRIBUTE_MAPPER.put(BOW, BowAttribute.BOW_ATTRIBUTES);
        ATTRIBUTE_MAPPER.put(SHIELD, ShieldAttribute.SHIELD_ATTRIBUTES);
    }

    private final String baseTranslateKey;

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
