package com.thexfactor117.levels.common.leveling;

import com.thexfactor117.levels.common.collections.RandomCollection;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.nbt.INBT;
import lombok.Getter;

import java.util.Random;

/**
 *
 * @author TheXFactor117
 *
 */
@Getter
public enum Rarity {
    DEFAULT(LegacyTextColor.GRAY, 0, 0, 0),
    COMMON(LegacyTextColor.WHITE, 0.65, 0xF0100010, 0x50FFFFFF),
    UNCOMMON(LegacyTextColor.DARK_GREEN, 0.2, 0xF0100010, 0x5000AA00),
    RARE(LegacyTextColor.AQUA, 0.1, 0xF0100010, 0x5055FFFF),
    LEGENDARY(LegacyTextColor.DARK_PURPLE, 0.045, 0xF0100010, 0x50AA00AA),
    MYTHIC(LegacyTextColor.GOLD, 0.005, 0xF0100010, 0x50FFAA00);

    public static final RandomCollection<Rarity> RARITY_RANDOM = new RandomCollection<>();

    static {
        for (Rarity entry : values()) {
            RARITY_RANDOM.add(entry.defaultChance, entry);
        }
    }

    private final String color;
    private final int hex;
    private final double defaultChance;
    private final int backgroundColor;
    private final int borderColor;

    Rarity(LegacyTextColor color, double chance, int backgroundColor, int borderColor) {
        this.color = color.toString();
        this.hex = color.getHex();
        this.defaultChance = chance;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
    }

    /**
     * Returns a randomized rarity based on the Blacksmithing rank of the player.
     * @param rand
     * @return
     */
    public static Rarity getRandomRarity(Random rand) {
        return RARITY_RANDOM.next(rand);
    }

    /**
     * Return the current rarity in the given NBTTagCompound. Returns Common if it can't find it.
     * @param nbt
     * @return
     */
    public static Rarity getRarity(INBT nbt) {
        return nbt != null && nbt.hasKey("RARITY") ? Rarity.values()[nbt.getInt("RARITY")] : DEFAULT;
    }

    /**
     * Sets the rarity specified to the given NBTTagCompound.
     * @param nbt
     */
    public void setRarity(INBT nbt) {
        if (nbt != null) {
            nbt.setInt("RARITY", this.ordinal());
        }
    }

}
