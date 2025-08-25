package com.thexfactor117.levels.forge.leveling.attributes.components;

import net.minecraft.nbt.NBTTagCompound;

public interface AttributeHolder {
    /**
     * @return true if the NBT tag compound has the specified Attribute.
     */
    boolean hasAttribute();

    /**
     * Adds the specified Attribute to the NBT tag compound.
     */
    default void addAttribute() {
        setAttributeTier(1);
    }

    /**
     * Removes the specified Attribute from the NBT tag compound.
     */
    void removeAttribute();

    /**
     * Sets the tier of the specific attribute.
     * @param tier
     */
    void setAttributeTier(int tier);

    /**
     * @return the tier of the specific attribute.
     */
    int getAttributeTier();

    String getAttributeKey();
}
