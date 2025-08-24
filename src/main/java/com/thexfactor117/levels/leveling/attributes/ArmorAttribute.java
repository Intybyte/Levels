package com.thexfactor117.levels.leveling.attributes;

import com.thexfactor117.levels.config.Config;
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
public enum ArmorAttribute {
    FIRE("Fire", Config.armorFire, TextFormatting.RED, 0xFF5555, AttributeRarity.UNCOMMON),
    FROST("Frost", Config.armorFrost, TextFormatting.AQUA, 0x55FFFFF, AttributeRarity.UNCOMMON),
    POISON("Poison", Config.armorPoison, TextFormatting.DARK_GREEN, 0x00AA00, AttributeRarity.UNCOMMON),
    DURABLE("Durable", Config.armorDurable, TextFormatting.GRAY, 0xAAAAAA, AttributeRarity.UNCOMMON),
    MAGICAL("Magical", Config.armorMagical, TextFormatting.BLUE, 0x5555FF, AttributeRarity.RARE),
    SOUL_BOUND("Soul Bound", Config.armorSoulBound, TextFormatting.DARK_PURPLE, 0xAA00AA, AttributeRarity.RARE),
    UNBREAKABLE("Unbreakable", Config.armorUnbreakable, TextFormatting.GRAY, 0xAAAAAA, AttributeRarity.LEGENDARY);

    private final String baseName;
    private final boolean enabled;
    private final String color;
    private final int hex;
    private final AttributeRarity rarity;

    public static ArrayList<ArmorAttribute> ARMOR_ATTRIBUTES = new ArrayList<ArmorAttribute>();

    ArmorAttribute(String baseName, boolean enabled, TextFormatting color, int hex, AttributeRarity rarity) {
        this.baseName = baseName;
        this.enabled = enabled;
        this.color = color.toString();
        this.hex = hex;
        this.rarity = rarity;
    }

    /**
     * Returns true if the NBT tag compound has the specified Attribute.
     * @param nbt
     * @return
     */
    public boolean hasAttribute(NBTTagCompound nbt) {
        return nbt != null && nbt.getBoolean(toString());
    }

    /**
     * Adds the specified Attribute to the NBT tag compound.
     * @param nbt
     */
    public void addAttribute(NBTTagCompound nbt) {
        if (nbt != null) {
            nbt.setBoolean(toString(), true);
            nbt.setInteger(baseName + "_TIER", 1);
        }
    }

    /**
     * Removes the specified Attribute from the NBT tag compound.
     * @param nbt
     */
    public void removeAttribute(NBTTagCompound nbt) {
        if (nbt != null) {
            nbt.removeTag(toString());
            nbt.removeTag(baseName + "_TIER");
        }
    }

    /**
     * Sets the tier of the specific attribute.
     * @param nbt
     * @param tier
     */
    public void setAttributeTier(NBTTagCompound nbt, int tier) {
        if (nbt != null) {
            nbt.setInteger(baseName + "_TIER", tier);
        }
    }

    /**
     * Returns the tier of the specific attribute.
     * @param nbt
     * @return
     */
    public int getAttributeTier(NBTTagCompound nbt) {
        return nbt != null ? nbt.getInteger(baseName + "_TIER") : 0;
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
        for (int i = 0; i < ArmorAttribute.values().length; i++) {
            if (ArmorAttribute.values()[i].enabled) {
                ArmorAttribute.ARMOR_ATTRIBUTES.add(ArmorAttribute.values()[i]);
            }
        }
    }
}
