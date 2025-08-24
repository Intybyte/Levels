package com.thexfactor117.levels.leveling.attributes.components;

import net.minecraft.nbt.NBTTagCompound;

public interface RomanNumeralDisplay {
    String getName(NBTTagCompound nbt);
    int getHexColor();
    String getColor();
}
