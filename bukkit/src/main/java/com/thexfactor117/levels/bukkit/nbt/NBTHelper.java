package com.thexfactor117.levels.bukkit.nbt;

import org.bukkit.persistence.PersistentDataContainer;

/**
 *
 * @author TheXFactor117
 *
 */
public class NBTHelper {
    public static NBTImpl toCommon(PersistentDataContainer nbt) {
        if (nbt == null) return null;

        return new NBTImpl(nbt);
    }
}
