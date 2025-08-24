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
public enum BowAttribute implements AttributeBase {
    FIRE("Fire", Config.bowFire, TextFormatting.RED, 0xFF5555, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.bowFrost, TextFormatting.AQUA, 0x55FFFF, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.bowPoison, TextFormatting.DARK_GREEN, 0x00AA00, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.bowDurable, TextFormatting.GRAY, 0xAAAAAA, AttributeRarity.UNCOMMON),
    ABSORB("Absorb", Config.bowAbsorb, TextFormatting.GREEN, 0x55FF55, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", Config.bowSoulBound, TextFormatting.DARK_PURPLE, 0xAA00AA, AttributeRarity.RARE),
    CRITICAL("Critical", Config.bowCritical, TextFormatting.BLUE, 0x5555FF, AttributeRarity.RARE),
    RECOVER("Recover", Config.bowRecover, TextFormatting.DARK_AQUA, 0x00AAAA, AttributeRarity.RARE),
    BARRAGE("Barrage", Config.bowBarrage, TextFormatting.DARK_RED, 0xAA0000, AttributeRarity.LEGENDARY),
    UNBREAKABLE("Unbreakable", Config.bowUnbreakable, TextFormatting.GRAY, 0xAAAAAA, AttributeRarity.LEGENDARY),
    VOID("Void", Config.bowVoid, TextFormatting.DARK_GRAY, 0x555555, AttributeRarity.LEGENDARY);

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hexColor;
    private final AttributeRarity rarity;

    public static ArrayList<BowAttribute> BOW_ATTRIBUTES = new ArrayList<>();

    BowAttribute(String baseName, boolean enabled, TextFormatting color, int hexColor, AttributeRarity rarity) {
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
        for (int i = 0; i < BowAttribute.values().length; i++) {
            if (BowAttribute.values()[i].enabled) {
                BowAttribute.BOW_ATTRIBUTES.add(BowAttribute.values()[i]);
            }
        }
    }
}
