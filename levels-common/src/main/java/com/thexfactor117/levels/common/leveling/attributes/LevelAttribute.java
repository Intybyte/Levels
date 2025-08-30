package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.leveling.attributes.components.config.ChanceConfigAttribute;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class LevelAttribute implements AttributeBase, SimpleConfigAttribute, ChanceConfigAttribute {
    private final double defaultBaseValue;
    private final double defaultMultiplier;
    private final int defaultMaxLevel;

    private final double defaultChance;

    private final SimpleAttribute wrappedAttribute;

    @Override
    public double getCalculatedValue(INBT nbt) {
        return getCalculatedValue(getAttributeTier(nbt));
    }

    @Override
    public ConfigEntry getEntry() {
        ConfigEntry entry = SimpleConfigAttribute.super.getEntry();
        ConfigEntry entryChance = ChanceConfigAttribute.super.getEntry();

        Map<String, String> map = entryChance.getMap();
        map.putAll(entry.getMap());

        return new ConfigEntry(map);
    }

    @Override
    public String getBaseKey() {
        return wrappedAttribute.baseKey;
    }

    @Override
    public AttributeRarity getRarity() {
        return wrappedAttribute.rarity;
    }

    @Override
    public boolean isEnabled() {
        return wrappedAttribute.isEnabled();
    }

    @Override
    public String getTranslationKey() {
        return wrappedAttribute.translationKey;
    }

    @Override
    public ItemType[] getAllowedTypes() {
        return wrappedAttribute.allowedTypes;
    }

    @Override
    public String getBaseName() {
        return wrappedAttribute.baseName;
    }

    @Override
    public int getHexColor() {
        return wrappedAttribute.getHexColor();
    }

    @Override
    public String getColor() {
        return wrappedAttribute.getColor();
    }
}
