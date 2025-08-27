package com.thexfactor117.levels.common.leveling;

import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.ShieldAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.utils.FieldProcessor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Used to get all the enabled attributes of an item
 */
@Getter
public enum ItemType {
    SWORD,
    ARMOR,
    BOW,
    SHIELD;

    private static final Map<ItemType, List<AttributeBase>> ATTRIBUTE_MAPPER = new EnumMap<>(ItemType.class);

    static {
        List<AttributeBase> list = FieldProcessor.getFields(AttributeBase.class, SwordAttribute.class, ArmorAttribute.class, BowAttribute.class, ShieldAttribute.class);
        for (ItemType type : values()) {
            ATTRIBUTE_MAPPER.put(type, new ArrayList<>());
        }

        for (AttributeBase attr : list) {
            for (ItemType type : attr.getAllowedTypes()) {
                ATTRIBUTE_MAPPER.get(type).add(attr);
            }
        }
    }

    public List<AttributeBase> attributes() {
        return ATTRIBUTE_MAPPER.get(this)
                .stream()
                .filter(AttributeBase::isEnabled)
                .collect(Collectors.toList());
    }
}
