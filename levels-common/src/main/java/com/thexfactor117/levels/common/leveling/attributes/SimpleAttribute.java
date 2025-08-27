package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.leveling.attributes.components.config.EnableConfigAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SimpleAttribute implements AttributeBase, EnableConfigAttribute {
    protected final String baseKey;

    protected final String baseName;
    protected final LegacyTextColor textColor;
    protected final AttributeRarity rarity;

    protected final ItemType[] allowedTypes;
    protected final String translationKey;

    @Override
    public boolean isEnabled() {
        return EnableConfigAttribute.super.isEnabled();
    }

    @Override
    public int getHexColor() {
        return textColor.getHex();
    }

    @Override
    public String getColor() {
        return textColor.toString();
    }
}
