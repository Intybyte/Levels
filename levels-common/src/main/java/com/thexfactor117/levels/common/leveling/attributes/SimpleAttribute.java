package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.leveling.attributes.components.config.EnableConfigAttribute;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleAttribute implements AttributeBase, EnableConfigAttribute {
    protected final String baseKey;

    protected final String baseName;
    protected final String color;
    protected final int hexColor;
    protected final AttributeRarity rarity;

    protected final ItemType[] allowedTypes;
    protected final String translationKey;

    @Builder
    public SimpleAttribute(String baseKey, String baseName, LegacyTextColor color, AttributeRarity rarity, ItemType[] allowedTypes, String translationKey) {
        this.baseName = baseName;
        this.color = color.toString();
        this.hexColor = color.getHex();
        this.rarity = rarity;

        this.baseKey = baseKey;
        this.allowedTypes = allowedTypes;
        this.translationKey = translationKey;
    }

    @Override
    public boolean isEnabled() {
        return EnableConfigAttribute.super.isEnabled();
    }
}
