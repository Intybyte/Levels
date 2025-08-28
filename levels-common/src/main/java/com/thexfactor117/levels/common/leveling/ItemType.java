package com.thexfactor117.levels.common.leveling;

import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttributes;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.utils.FieldProcessor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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

    public static void init() {

        List<AttributeBase> list = FieldProcessor.getFields(
                AttributeBase.class,
                SwordAttribute.class,
                ArmorAttribute.class,
                BowAttribute.class,
                AnyAttributes.class,
                WeaponAttributes.class
        );

        for (ItemType type : values()) {
            ATTRIBUTE_MAPPER.put(type, new ArrayList<>());
        }

        processAttributes(list);
    }

    public static void processAttributes(List<AttributeBase> list) {
        for (AttributeBase attr : list) {
            for (ItemType type : attr.getAllowedTypes()) {
                ATTRIBUTE_MAPPER.get(type).add(attr);
            }
        }

        for (ItemType type : values()) {
            ATTRIBUTE_MAPPER.get(type).sort(
                    Comparator.comparingInt(o -> o.getRarity().getCost())
            );
        }
    }

    public List<AttributeBase> enabledAttributes() {
        List<AttributeBase> enabled = new ArrayList<>();

        for (AttributeBase attr : ATTRIBUTE_MAPPER.get(this)) {
            if (attr.isEnabled()) {
                enabled.add(attr);
            }
        }

        return enabled;
    }

    public List<AttributeBase> attributesRaw() {
        return ATTRIBUTE_MAPPER.get(this);
    }
}
