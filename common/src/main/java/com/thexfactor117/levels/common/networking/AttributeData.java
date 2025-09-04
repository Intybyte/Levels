package com.thexfactor117.levels.common.networking;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

//TODO: send available attributes via network instead of relying on wrong client config
@Getter
@Builder
@AllArgsConstructor
public class AttributeData implements AttributeBase, SimpleConfigAttribute {
    public final String key;
    public final String baseName;
    public final String translationKey;
    public final AttributeRarity rarity;
    public final boolean enabled;

    public final int maxTier;
    public final double baseValue;
    public final double multiplier;

    public final int hexColor;
    public final String color;

    public double getCalculatedValue(int level) {
        double overallMultiplier = Math.pow(multiplier, level - 1);
        return baseValue * overallMultiplier;
    }

    public double getCalculatedValue(INBT nbt) {
        return getCalculatedValue(getAttributeTier(nbt));
    }

    @Override
    public int getMaxLevel() {
        return maxTier;
    }

    @Override
    public String getAttributeKey() {
        return key;
    }

    @Override
    public double getBaseValue() {
        return baseValue;
    }

    @Override
    public double getMultiplier() {
        return multiplier;
    }

    //region stub heaven
    @Override
    public ConfigEntry getEntry() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDefaultMaxLevel() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBaseKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDefaultBaseValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getDefaultMultiplier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemType[] getAllowedTypes() {
        throw new UnsupportedOperationException();
    }
    //endregion
}
