package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.Getter;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum ArmorAttribute implements AttributeBase, SimpleConfigAttribute {
    MAGICAL("Magical", LegacyTextColor.BLUE, AttributeRarity.RARE, 0.2, 1.5, 3);

    private final String baseKey;

    private final String baseName;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    private final double defaultBaseValue;
    private final double defaultMultiplier;
    private final int defaultMaxLevel;

    ArmorAttribute(String baseName, LegacyTextColor color, AttributeRarity rarity, double defaultBaseValue, double defaultMultiplier, int defaultMaxLevel) {
        this.baseName = baseName;
        this.color = color.toString();
        this.hexColor = color.getHex();
        this.rarity = rarity;

        this.defaultBaseValue = defaultBaseValue;
        this.defaultMultiplier = defaultMultiplier;
        this.defaultMaxLevel = defaultMaxLevel;

        this.baseKey = SimpleConfigAttribute.keyOf(this);
    }

    @Override
    public boolean isEnabled() {
        return SimpleConfigAttribute.super.isEnabled();
    }

    @Override
    public String getTranslationKey() {
        return "levels.attributes.armors.info." + ordinal();
    }

    @Override
    public ItemType[] getAllowedTypes() {
        return new ItemType[] { ItemType.ARMOR };
    }

    public double getCalculatedValue(INBT nbt) {
        return getCalculatedValue(getAttributeTier(nbt));
    }
}
