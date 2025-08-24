package com.thexfactor117.levels.forge.leveling.attributes;

import com.thexfactor117.levels.common.LegacyTextColor;
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
public enum ArmorAttribute implements AttributeBase {
    FIRE("Fire", Config.armorFire, LegacyTextColor.RED, 0xFF5555, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.armorFrost, LegacyTextColor.AQUA, 0x55FFFFF, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.armorPoison, LegacyTextColor.DARK_GREEN, 0x00AA00, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.armorDurable, LegacyTextColor.GRAY, 0xAAAAAA, AttributeRarity.UNCOMMON),
    MAGICAL("Magical", Config.armorMagical, LegacyTextColor.BLUE, 0x5555FF, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", Config.armorSoulBound, LegacyTextColor.DARK_PURPLE, 0xAA00AA, AttributeRarity.RARE),
    UNBREAKABLE("Unbreakable", Config.armorUnbreakable, LegacyTextColor.GRAY, 0xAAAAAA, AttributeRarity.LEGENDARY);

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    public static ArrayList<ArmorAttribute> ARMOR_ATTRIBUTES = new ArrayList<>();

    ArmorAttribute(String baseName, boolean enabled, LegacyTextColor color, int hexColor, AttributeRarity rarity) {
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

    static {
        for (int i = 0; i < ArmorAttribute.values().length; i++) {
            if (ArmorAttribute.values()[i].enabled) {
                ArmorAttribute.ARMOR_ATTRIBUTES.add(ArmorAttribute.values()[i]);
            }
        }
    }
}
