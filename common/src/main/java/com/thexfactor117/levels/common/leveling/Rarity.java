package com.thexfactor117.levels.common.leveling;

import com.thexfactor117.levels.common.collections.RandomCollection;
import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.attributes.display.Formatter;
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

    public static final String RARITY_KEY = "levels:rarity";

    static {
        for (Rarity entry : values()) {
            if (entry != DEFAULT) {
                RARITY_RANDOM.add(entry.defaultChance, entry);
            }
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

    public String getDisplay(Formatter formatter) {
        return color + LegacyTextColor.Style.ITALIC + formatter.format("levels.rarities." + ordinal());
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
        return nbt != null && nbt.hasKey(RARITY_KEY) ? Rarity.values()[nbt.getInt(RARITY_KEY)] : DEFAULT;
    }

    /**
     * Sets the rarity specified to the given NBTTagCompound.
     * @param nbt
     */
    public void setRarity(INBT nbt) {
        if (nbt != null) {
            nbt.setInt(RARITY_KEY, this.ordinal());
        }
    }

    /*
     * Stuff for calculating xp and repair, previously called "useRarity"
     */


    /**
     * Calculates bonus XP based on rarity and if it was a death/kill event.
     *
     * @return The amount of XP to add, or 0 if none.
     */
    public int generateExperience() {
        if (this == Rarity.DEFAULT) return 0;

        int chanceRoll, xp;

        switch (this) {
            case UNCOMMON: // 1 in 15 chance to add 1–3 XP
                chanceRoll = (int) (Math.random() * 15);
                xp = (int) (Math.random() * 3 + 1);
                return chanceRoll == 0 ? xp : 0;

            case RARE: // 1 in 10 chance to add 1–5 XP
                chanceRoll = (int) (Math.random() * 10);
                xp = (int) (Math.random() * 5 + 1);
                return chanceRoll == 0 ? xp : 0;

            case LEGENDARY: // 1 in 7 chance to add 3–7 XP
                chanceRoll = (int) (Math.random() * 7);
                xp = (int) (Math.random() * 5 + 3);
                return chanceRoll == 0 ? xp : 0;

            case MYTHIC: // 1 in 5 chance to add 3–10 XP
                chanceRoll = (int) (Math.random() * 5);
                xp = (int) (Math.random() * 8 + 3);
                return chanceRoll == 0 ? xp : 0;

            default:
                return 0;
        }
    }


    /**
     * Calculates durability repair bonus based on rarity and usage event.
     *
     * @return Durability points to *repair* (subtract from damage), or 0 if none.
     */
    public int generateRarityRepair() {
        if (this == Rarity.DEFAULT) return 0;

        int chanceRoll, repair;

        switch (this) {
            case UNCOMMON: // 1 in 15 chance to negate 1 durability loss
                chanceRoll = (int) (Math.random() * 15);
                return chanceRoll == 0 ? 1 : 0;

            case RARE: // 1 in 10 chance to negate 1 durability + repair 0–1
                chanceRoll = (int) (Math.random() * 10);
                repair = (int) (Math.random() * 2);
                return chanceRoll == 0 ? (1 + repair) : 0;

            case LEGENDARY: // 1 in 7 chance to negate 1 durability + repair 1–3
                chanceRoll = (int) (Math.random() * 7);
                repair = (int) (Math.random() * 3 + 1);
                return chanceRoll == 0 ? (1 + repair) : 0;

            case MYTHIC: // 1 in 5 chance to negate 1 durability + repair 1–5
                chanceRoll = (int) (Math.random() * 5);
                repair = (int) (Math.random() * 5 + 1);
                return chanceRoll == 0 ? (1 + repair) : 0;

            default:
                return 0;
        }
    }


    /*
     * Stuff for rarity attribute calculation and similar
     */

    public double generateWeightedDamage(double baseDamage) {
        if (this == DEFAULT) return baseDamage;

        int range = this.ordinal() + 2; // common(1) + 2 = 3
        int addAttackBase = this.ordinal() - 3; // common(1) - 3 = -2

        return Math.random() * range + (baseDamage + addAttackBase);
    }

    public double generateWeightedAttackSpeed(double baseAttackSpeed) {
        if (this == DEFAULT) return baseAttackSpeed;

        double ordinalDouble = this.ordinal();
        double range = (ordinalDouble + 1) / 10; // ( common(1) + 1 ) / 10 = 0.2
        double addAttackSpeed = (ordinalDouble - 3) / 10; // ( common(1) -3 ) / 10 = -0.2

        return Math.random() * range + (baseAttackSpeed + addAttackSpeed);
    }

    public double generateWeightedArmorToughness(double baseToughness) {
        double result = generateWeightedAttackSpeed(baseToughness); // same calculation

        if (result < 0) return 0;

        return result;
    }

    public double getWeightedArmor(double baseArmor) {
        return generateWeightedArmorToughness(baseArmor); // same calculation
    }

    /**
     * The cubic function that calculates multiplier range
     * where x = ordinal() - 1 (considering common as start point)
     * was created using <a href="https://www.omnicalculator.com/statistics/cubic-regression">cubic from points</a>
     * y = 0.0499 + 0.0235x + 0.0064x2 + 0.0008x3
     * <br>
     * ## POINTS USED ##:
     * COMMON: range = 0.05
     * UNCOMMON: range = 0.08
     * RARE: range = 0.13
     * LEGENDARY: range = 0.2
     * MYTHIC: range = 0.3
     */
    public double generateWeightedMultiplier() {
        double x = this.ordinal() - 1;

        double range = 0.499 + 0.0235 * x + 0.0064 * Math.pow(x, 2) + 0.0008 * Math.pow(x, 3);

        return Math.random() * range;
    }
}
