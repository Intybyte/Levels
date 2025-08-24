package com.thexfactor117.levels.leveling.attributes;

import com.thexfactor117.levels.config.Config;
import com.thexfactor117.levels.leveling.attributes.components.AttributeBase;
import com.thexfactor117.levels.leveling.attributes.components.AttributeRarity;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum WeaponAttribute implements AttributeBase {
    FIRE("Fire", Config.weaponFire, TextFormatting.RED, 0xFF5555, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.weaponFrost, TextFormatting.AQUA, 0x55FFFF, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.weaponPoison, TextFormatting.DARK_GREEN, 0x00AA00, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.weaponDurable, TextFormatting.GRAY, 0xAAAAAA, AttributeRarity.UNCOMMON),
    ABSORB("Absorb", Config.weaponAbsorb, TextFormatting.GREEN, 0x55FF55, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", Config.weaponSoulBound, TextFormatting.DARK_PURPLE, 0xAA00AA, AttributeRarity.RARE),
    CRITICAL("Critical", Config.weaponCritical, TextFormatting.BLUE, 0x5555FF, AttributeRarity.RARE),
    CHAINED("Chained", Config.weaponChained, TextFormatting.WHITE, 0xFFFFFF, AttributeRarity.LEGENDARY),
    UNBREAKABLE("Unbreakable", Config.weaponUnbreakable, TextFormatting.GRAY, 0xAAAAAA, AttributeRarity.LEGENDARY),
    VOID("Void", Config.weaponVoid, TextFormatting.DARK_GRAY, 0x555555, AttributeRarity.LEGENDARY);

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hex;
    private final AttributeRarity rarity;

    public static final ArrayList<WeaponAttribute> WEAPON_ATTRIBUTES = new ArrayList<>();

    WeaponAttribute(String baseName, boolean enabled, Object color, int hex, AttributeRarity rarity) {
        this.baseName = baseName;
        this.enabled = enabled;
        this.color = color.toString();
        this.hex = hex;
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

    public String getName(NBTTagCompound nbt) {
        if (getAttributeTier(nbt) == 1)
            return baseName;
        else if (getAttributeTier(nbt) == 2)
            return baseName + " II";
        else if (getAttributeTier(nbt) == 3)
            return baseName + " III";
        else
            return baseName;
    }

    static {
        for (int i = 0; i < WeaponAttribute.values().length; i++) {
            if (WeaponAttribute.values()[i].enabled) {
                WeaponAttribute.WEAPON_ATTRIBUTES.add(WeaponAttribute.values()[i]);
            }
        }
    }
}
