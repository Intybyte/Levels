package com.thexfactor117.levels.leveling.attributes.components;

import net.minecraft.nbt.NBTTagCompound;

public interface AttributeBase {

    /**
     * Returns true if the NBT tag compound has the specified Attribute.
     * @param nbt
     * @return
     */
    default boolean hasAttribute(NBTTagCompound nbt) {
        return nbt != null && nbt.hasKey(getAttributeKey());
    }

    /**
     * Adds the specified Attribute to the NBT tag compound.
     * @param nbt
     */
    default void addAttribute(NBTTagCompound nbt) {
        if (nbt != null) {
            nbt.setInteger(getAttributeKey(), 1);
        }
    }

    /**
     * Removes the specified Attribute from the NBT tag compound.
     * @param nbt
     */
    default void removeAttribute(NBTTagCompound nbt) {
        if (nbt != null) {
            nbt.removeTag(getAttributeKey());
        }
    }

    /**
     * Sets the tier of the specific attribute.
     * @param nbt
     * @param tier
     */
    default void setAttributeTier(NBTTagCompound nbt, int tier) {
        if (nbt != null) {
            nbt.setInteger(getAttributeKey(), tier);
        }
    }

    /**
     * Returns the tier of the specific attribute.
     * @param nbt
     * @return
     */
    default int getAttributeTier(NBTTagCompound nbt) {
        return nbt != null ? nbt.getInteger(getAttributeKey()) : 0;
    }

    String getAttributeKey();

    AttributeRarity getRarity();
}
