package com.thexfactor117.levels.forge.leveling.attributes;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.forge.config.Config;
import com.thexfactor117.levels.forge.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.common.attribute.AttributeRarity;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum ShieldAttribute implements AttributeBase {
    FIRE("Fire", Config.shieldFire, LegacyTextColor.RED, 0xFF5555, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.shieldFrost, LegacyTextColor.AQUA, 0x55FFFF, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.shieldPoison, LegacyTextColor.DARK_GREEN, 0x00AA00, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.shieldDurable, LegacyTextColor.GRAY, 0xAAAAAA, AttributeRarity.UNCOMMON),
    SOUL_BOUND("Soul Bound", Config.shieldSoulBound, LegacyTextColor.DARK_PURPLE, 0xAA00AA, AttributeRarity.RARE),
    UNBREAKABLE("Unbreakable", Config.shieldUnbreakable, LegacyTextColor.GRAY, 0xAAAAAA, AttributeRarity.LEGENDARY);

    public static final ArrayList<ShieldAttribute> SHIELD_ATTRIBUTES = new ArrayList<>();

    static {
        for (int i = 0; i < ShieldAttribute.values().length; i++) {
            if (ShieldAttribute.values()[i].enabled) {
                ShieldAttribute.SHIELD_ATTRIBUTES.add(ShieldAttribute.values()[i]);
            }
        }
    }

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    ShieldAttribute(String baseName, boolean enabled, LegacyTextColor color, int hexColor, AttributeRarity rarity) {
        this.baseName = baseName;
        this.enabled = enabled;
        this.color = color.toString();
        this.hexColor = hexColor;
        this.rarity = rarity;
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
}
