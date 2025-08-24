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
public enum WeaponAttribute implements AttributeBase {
    FIRE("Fire", Config.weaponFire, LegacyTextColor.RED, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.weaponFrost, LegacyTextColor.AQUA, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.weaponPoison, LegacyTextColor.DARK_GREEN, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.weaponDurable, LegacyTextColor.GRAY, AttributeRarity.UNCOMMON),
    ABSORB("Absorb", Config.weaponAbsorb, LegacyTextColor.GREEN, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", Config.weaponSoulBound, LegacyTextColor.DARK_PURPLE, AttributeRarity.RARE),
    CRITICAL("Critical", Config.weaponCritical, LegacyTextColor.BLUE, AttributeRarity.RARE),
    CHAINED("Chained", Config.weaponChained, LegacyTextColor.WHITE, AttributeRarity.LEGENDARY),
    UNBREAKABLE("Unbreakable", Config.weaponUnbreakable, LegacyTextColor.GRAY, AttributeRarity.LEGENDARY),
    VOID("Void", Config.weaponVoid, LegacyTextColor.DARK_GRAY, AttributeRarity.LEGENDARY);

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    public static final ArrayList<WeaponAttribute> WEAPON_ATTRIBUTES = new ArrayList<>();

    WeaponAttribute(String baseName, boolean enabled, LegacyTextColor color, AttributeRarity rarity) {
        this.baseName = baseName;
        this.enabled = enabled;
        this.color = color.toString();
        this.hexColor = color.getHex();
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
        for (int i = 0; i < WeaponAttribute.values().length; i++) {
            if (WeaponAttribute.values()[i].enabled) {
                WeaponAttribute.WEAPON_ATTRIBUTES.add(WeaponAttribute.values()[i]);
            }
        }
    }
}
