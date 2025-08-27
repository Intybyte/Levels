package com.thexfactor117.levels.common.leveling;

import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import lombok.Getter;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Used to get all the enabled attributes of an item
 */
@Getter
public enum ItemType {
    SWORD,
    ARMOR,
    BOW,
    SHIELD;

    private static final Map<ItemType, Supplier<List<? extends AttributeBase>>> ATTRIBUTE_MAPPER = new EnumMap<>(ItemType.class);

    static {
        ATTRIBUTE_MAPPER.put(SWORD, SwordAttribute::getEnabled);
        ATTRIBUTE_MAPPER.put(ARMOR, ArmorAttribute::getEnabled);
        ATTRIBUTE_MAPPER.put(BOW, BowAttribute::getEnabled);
        ATTRIBUTE_MAPPER.put(SHIELD, ShieldAttribute::getEnabled);
    }

    public List<? extends AttributeBase> attributes() {
        return ATTRIBUTE_MAPPER.get(this).get();
    }
}
