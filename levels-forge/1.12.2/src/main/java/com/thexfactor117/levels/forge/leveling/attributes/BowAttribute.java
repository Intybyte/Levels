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
public enum BowAttribute implements AttributeBase {
    FIRE("Fire", Config.bowFire, LegacyTextColor.RED, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.bowFrost, LegacyTextColor.AQUA, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.bowPoison, LegacyTextColor.DARK_GREEN, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.bowDurable, LegacyTextColor.GRAY, AttributeRarity.UNCOMMON),
    ABSORB("Absorb", Config.bowAbsorb, LegacyTextColor.GREEN, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", Config.bowSoulBound, LegacyTextColor.DARK_PURPLE, AttributeRarity.RARE),
    CRITICAL("Critical", Config.bowCritical, LegacyTextColor.BLUE, AttributeRarity.RARE),
    RECOVER("Recover", Config.bowRecover, LegacyTextColor.DARK_AQUA, AttributeRarity.RARE),
    BARRAGE("Barrage", Config.bowBarrage, LegacyTextColor.DARK_RED, AttributeRarity.LEGENDARY),
    UNBREAKABLE("Unbreakable", Config.bowUnbreakable, LegacyTextColor.GRAY, AttributeRarity.LEGENDARY),
    VOID("Void", Config.bowVoid, LegacyTextColor.DARK_GRAY, AttributeRarity.LEGENDARY);

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    public static ArrayList<BowAttribute> BOW_ATTRIBUTES = new ArrayList<>();

    BowAttribute(String baseName, boolean enabled, LegacyTextColor color, AttributeRarity rarity) {
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
        for (int i = 0; i < BowAttribute.values().length; i++) {
            if (BowAttribute.values()[i].enabled) {
                BowAttribute.BOW_ATTRIBUTES.add(BowAttribute.values()[i]);
            }
        }
    }
}
