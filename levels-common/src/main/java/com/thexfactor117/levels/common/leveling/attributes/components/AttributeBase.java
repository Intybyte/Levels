package com.thexfactor117.levels.common.leveling.attributes.components;

import com.thexfactor117.levels.common.nbt.INBT;

public interface AttributeBase extends RomanNumeralDisplay, EnableAttribute {

    /**
     * Returns true if the NBT tag compound has the specified Attribute.
     * @param nbt
     * @return
     */
    default boolean hasAttribute(INBT nbt) {
        return nbt != null && nbt.hasKey(getAttributeKey());
    }

    /**
     * Adds the specified Attribute to the NBT tag compound.
     * @param nbt
     */
    default void addAttribute(INBT nbt) {
        if (nbt != null) {
            nbt.setInt(getAttributeKey(), 1);
        }
    }

    /**
     * Removes the specified Attribute from the NBT tag compound.
     * @param nbt
     */
    default void removeAttribute(INBT nbt) {
        if (nbt != null) {
            nbt.remove(getAttributeKey());
        }
    }

    /**
     * Sets the tier of the specific attribute.
     * @param nbt
     * @param tier
     */
    default void setAttributeTier(INBT nbt, int tier) {
        if (nbt != null) {
            nbt.setInt(getAttributeKey(), tier);
        }
    }

    /**
     * Returns the tier of the specific attribute.
     * @param nbt
     * @return
     */
    default int getAttributeTier(INBT nbt) {
        return nbt != null ? nbt.getInt(getAttributeKey()) : 0;
    }

    default double getCalculatedValue(INBT nbt, double baseValue, double multiplier) {
        if (getAttributeTier(nbt) == 1)
            return baseValue;
        else if (getAttributeTier(nbt) == 2)
            return baseValue * multiplier;
        else
            return baseValue * (Math.pow(multiplier, 2));
    }

    String getAttributeKey();

    AttributeRarity getRarity();

    default String getName(INBT nbt) {
        return getCompleteName(getAttributeTier(nbt));
    }
}