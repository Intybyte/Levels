package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.EnableAttribute;
import com.thexfactor117.levels.common.leveling.attributes.components.SimpleConfigAttribute;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import lombok.Getter;

import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum ArmorAttribute implements AttributeBase, SimpleConfigAttribute {
    FIRE("Fire", LegacyTextColor.RED, AttributeRarity.UNCOMMON),
    FROST("Frost", LegacyTextColor.AQUA, AttributeRarity.UNCOMMON),
    POISON("Poison", LegacyTextColor.DARK_GREEN, AttributeRarity.UNCOMMON),
    DURABLE("Durable", LegacyTextColor.GRAY, AttributeRarity.UNCOMMON),
    MAGICAL("Magical", LegacyTextColor.BLUE, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", LegacyTextColor.DARK_PURPLE, AttributeRarity.RARE),
    UNBREAKABLE("Unbreakable", LegacyTextColor.GRAY, AttributeRarity.LEGENDARY);

    private final String enabledKey;

    private final String baseName;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    ArmorAttribute(String baseName, LegacyTextColor color, AttributeRarity rarity) {
        this.baseName = baseName;
        this.color = color.toString();
        this.hexColor = color.getHex();
        this.rarity = rarity;

        this.enabledKey = SimpleConfigAttribute.keyOf(this);
    }

    @Override
    public String getAttributeKey() {
        return baseName + "_TIER";
    }

    @Override
    public boolean isEnabled() {
        return SimpleConfigAttribute.super.isEnabled();
    }

    public static List<ArmorAttribute> getEnabled() {
        return EnableAttribute.getEnabled(values());
    }
}
