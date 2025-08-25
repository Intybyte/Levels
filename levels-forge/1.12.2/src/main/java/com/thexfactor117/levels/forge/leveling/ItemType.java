package com.thexfactor117.levels.forge.leveling;

import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttribute;
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
import java.util.function.Supplier;

/**
 * Used to get all the enabled attributes of an item
 */
@AllArgsConstructor
@Getter
public enum ItemType {
    SWORD("levels.attributes.weapons.info"),
    ARMOR("levels.attributes.armors.info"),
    BOW("levels.attributes.bows.info"),
    SHIELD("levels.attributes.shields.info");

    private static final Map<ItemType, Supplier<List<? extends AttributeBase>>> ATTRIBUTE_MAPPER = new EnumMap<>(ItemType.class);

    static {
        ATTRIBUTE_MAPPER.put(SWORD, WeaponAttribute::getEnabled);
        ATTRIBUTE_MAPPER.put(ARMOR, ArmorAttribute::getEnabled);
        ATTRIBUTE_MAPPER.put(BOW, BowAttribute::getEnabled);
        ATTRIBUTE_MAPPER.put(SHIELD, ShieldAttribute::getEnabled);
    }

    private final String baseTranslateKey;

    public static ItemType of(Item item) {
        if (item instanceof ItemSword) return SWORD;
        if (item instanceof ItemArmor) return ARMOR;
        if (item instanceof ItemBow) return BOW;
        if (item instanceof ItemShield) return SHIELD;
        return null;
    }

    public List<? extends AttributeBase> attributes() {
        return ATTRIBUTE_MAPPER.get(this).get();
    }
}
