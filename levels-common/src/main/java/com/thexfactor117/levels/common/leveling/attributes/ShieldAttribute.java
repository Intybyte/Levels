package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.leveling.attributes.components.EnableAttribute;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import com.thexfactor117.levels.common.color.LegacyTextColor;
;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.Getter;

import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum ShieldAttribute implements AttributeBase, SimpleConfigAttribute {
    FIRE("Fire", LegacyTextColor.RED, AttributeRarity.UNCOMMON, 4, 1.25),
    FROST("Frost", LegacyTextColor.AQUA, AttributeRarity.UNCOMMON, 20, 1.5),
    POISON("Poison", LegacyTextColor.DARK_GREEN, AttributeRarity.UNCOMMON, 20 * 7, 1.5),
    DURABLE("Durable", LegacyTextColor.GRAY, AttributeRarity.UNCOMMON, 1, 2),
    SOUL_BOUND("Soul Bound", LegacyTextColor.DARK_PURPLE, AttributeRarity.RARE, 0, 0),
    UNBREAKABLE("Unbreakable", LegacyTextColor.GRAY, AttributeRarity.LEGENDARY, 0, 0);

    private final String baseKey;

    private final String baseName;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    private final double defaultBaseValue;
    private final double defaultMultiplier;

    ShieldAttribute(String baseName, LegacyTextColor color, AttributeRarity rarity, double defaultBaseValue, double defaultMultiplier) {
        this.baseName = baseName;
        this.color = color.toString();
        this.hexColor = color.getHex();
        this.rarity = rarity;
        this.defaultBaseValue = defaultBaseValue;
        this.defaultMultiplier = defaultMultiplier;

        this.baseKey = SimpleConfigAttribute.keyOf(this);
    }

    @Override
    public boolean isEnabled() {
        return SimpleConfigAttribute.super.isEnabled();
    }

    public double getCalculatedValue(INBT nbt) {
        return getCalculatedValue(getAttributeTier(nbt));
    }

    public static List<ShieldAttribute> getEnabled() {
        return EnableAttribute.getEnabled(values());
    }
}
