package com.thexfactor117.levels.leveling.attributes;

import com.thexfactor117.levels.config.Config;
import com.thexfactor117.levels.leveling.Rarity;
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
public enum ShieldAttribute {
    FIRE("Fire", Config.shieldFire, TextFormatting.RED, 0xFF5555, Rarity.UNCOMMON),
    FROST("Frost", Config.shieldFrost, TextFormatting.AQUA, 0x55FFFF, Rarity.UNCOMMON),
    POISON("Poison", Config.shieldPoison, TextFormatting.DARK_GREEN, 0x00AA00, Rarity.UNCOMMON),
    DURABLE("Durable", Config.shieldDurable, TextFormatting.GRAY, 0xAAAAAA, Rarity.UNCOMMON),
    SOUL_BOUND("Soul Bound", Config.shieldSoulBound, TextFormatting.DARK_PURPLE, 0xAA00AA, Rarity.RARE),
    UNBREAKABLE("Unbreakable", Config.shieldUnbreakable, TextFormatting.GRAY, 0xAAAAAA, Rarity.LEGENDARY);

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
    private final int hex;
    private final Rarity rarity;

    ShieldAttribute(String name, boolean enabled, TextFormatting color, int hex, Rarity rarity) {
        this.baseName = name;
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

    public String getBaseName(NBTTagCompound nbt) {
        if (getAttributeTier(nbt) == 1)
            return baseName;
        else if (getAttributeTier(nbt) == 2)
            return baseName + " II";
        else if (getAttributeTier(nbt) == 3)
            return baseName + " III";
        else
            return baseName;
    }
}
