package com.thexfactor117.levels.forge.leveling;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.collections.RandomCollection;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 *
 * @author TheXFactor117
 *
 */
public enum Rarity {
    DEFAULT(LegacyTextColor.GRAY, 0, 0, 0, 0),
    COMMON(LegacyTextColor.WHITE, 0xFFFFFF, 0.65, 0xF0100010, 0x50FFFFFF),
    UNCOMMON(LegacyTextColor.DARK_GREEN, 0x00AA00, 0.2, 0xF0100010, 0x5000AA00),
    RARE(LegacyTextColor.AQUA, 0x55FFFF, 0.1, 0xF0100010, 0x5055FFFF),
    LEGENDARY(LegacyTextColor.DARK_PURPLE, 0xAA00AA, 0.045, 0xF0100010, 0x50AA00AA),
    MYTHIC(LegacyTextColor.GOLD, 0xFFAA00, 0.005, 0xF0100010, 0x50FFAA00);

    public static final RandomCollection<Rarity> rarityRandom = new RandomCollection<>();

    static {
        for (Rarity entry : values()) {
            rarityRandom.add(entry.defaultChance, entry);
        }
    }

    private final String color;
    private final int hex;
    private final double defaultChance;
    private final int backColor;
    private final int borderColor;

    Rarity(Object color, int hex, double chance, int backColor, int borderColor) {
        this.color = color.toString();
        this.hex = hex;
        this.defaultChance = chance;
        this.backColor = backColor;
        this.borderColor = borderColor;
    }

    /**
     * Returns a randomized rarity based on the Blacksmithing rank of the player.
     * @param nbt
     * @param rand
     * @return
     */
    public static Rarity getRandomRarity(NBTTagCompound nbt, Random rand) {
        return rarityRandom.next(rand);
    }

    /**
     * Return the current rarity in the given NBTTagCompound. Returns Common if it can't find it.
     * @param nbt
     * @return
     */
    public static Rarity getRarity(NBTTagCompound nbt) {
        return nbt != null && nbt.hasKey("RARITY") ? Rarity.values()[nbt.getInteger("RARITY")] : DEFAULT;
    }

    /**
     * Sets the rarity specified to the given NBTTagCompound.
     * @param nbt
     * @param rarity
     */
    public static void setRarity(NBTTagCompound nbt, Rarity rarity) {
        if (nbt != null) {
            nbt.setInteger("RARITY", rarity.ordinal());
        }
    }

    /*
     *
     * GETTERS AND SETTERS
     *
     */

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format("levels.rarities." + ordinal());
    }

    public String getColor() {
        return color;
    }

    public int getHex() {
        return hex;
    }

    public double getDefaultChance() {
        return defaultChance;
    }

    public int getBackgroundColor() {
        return backColor;
    }

    public int getBorderColor() {
        return borderColor;
    }
}
