package com.thexfactor117.levels.forge.leveling.attributes;

import com.thexfactor117.levels.common.attribute.EnableAttribute;
import com.thexfactor117.levels.common.attribute.SimpleConfigAttribute;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.forge.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.attribute.AttributeRarity;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum WeaponAttribute implements AttributeBase, SimpleConfigAttribute {
    FIRE("Fire", LegacyTextColor.RED, AttributeRarity.UNCOMMON),
    FROST("Frost", LegacyTextColor.AQUA, AttributeRarity.UNCOMMON),
    POISON("Poison", LegacyTextColor.DARK_GREEN, AttributeRarity.UNCOMMON),
    DURABLE("Durable", LegacyTextColor.GRAY, AttributeRarity.UNCOMMON),
    ABSORB("Absorb", LegacyTextColor.GREEN, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", LegacyTextColor.DARK_PURPLE, AttributeRarity.RARE),
    CRITICAL("Critical", LegacyTextColor.BLUE, AttributeRarity.RARE),
    CHAINED("Chained", LegacyTextColor.WHITE, AttributeRarity.LEGENDARY),
    UNBREAKABLE("Unbreakable", LegacyTextColor.GRAY, AttributeRarity.LEGENDARY),
    VOID("Void", LegacyTextColor.DARK_GRAY, AttributeRarity.LEGENDARY);

    private final String enabledKey;

    private final String baseName;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    WeaponAttribute(String baseName, LegacyTextColor color, AttributeRarity rarity) {
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

    public double getCalculatedValue(NBTTagCompound nbt, double baseValue, double multiplier) {
        if (getAttributeTier(nbt) == 1)
            return baseValue;
        else if (getAttributeTier(nbt) == 2)
            return baseValue * multiplier;
        else
            return baseValue * (Math.pow(multiplier, 2));

    }

    @Override
    public boolean isEnabled() {
        return SimpleConfigAttribute.super.isEnabled();
    }

    public static List<WeaponAttribute> getEnabled() {
        return EnableAttribute.getEnabled(values());
    }
}
