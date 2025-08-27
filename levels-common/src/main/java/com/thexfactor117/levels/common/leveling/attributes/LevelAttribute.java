package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LevelAttribute extends SimpleAttribute implements AttributeBase, SimpleConfigAttribute {
    private final double defaultBaseValue;
    private final double defaultMultiplier;
    private final int defaultMaxLevel;

    @Builder
    public LevelAttribute(String baseKey, String baseName, LegacyTextColor color, AttributeRarity rarity, ItemType[] allowedTypes, String translationKey, double defaultBaseValue, double defaultMultiplier, int defaultMaxLevel) {
        super(baseKey, baseName, color, rarity, allowedTypes, translationKey);
        this.defaultBaseValue = defaultBaseValue;
        this.defaultMultiplier = defaultMultiplier;
        this.defaultMaxLevel = defaultMaxLevel;
    }

    @Override
    public double getCalculatedValue(INBT nbt) {
        return getCalculatedValue(getAttributeTier(nbt));
    }
}
