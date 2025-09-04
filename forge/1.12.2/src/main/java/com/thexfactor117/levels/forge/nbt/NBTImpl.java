package com.thexfactor117.levels.forge.nbt;

import com.thexfactor117.levels.common.nbt.INBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.NBTTagCompound;

@Getter
@AllArgsConstructor
public class NBTImpl implements INBT {
    private final NBTTagCompound nbt;

    @Override
    public boolean hasKey(String key) {
        return nbt.hasKey(key);
    }

    @Override
    public int getInt(String key) {
        return nbt.getInteger(key);
    }

    @Override
    public void setInt(String key, int value) {
        nbt.setInteger(key, value);
    }

    /*
    @Override
    public double getDouble(String key) {
        return nbt.getDouble(key);
    }

    @Override
    public void setDouble(String key, double value) {
        nbt.setDouble(key, value);
    }*/

    @Override
    public void remove(String key) {
        nbt.removeTag(key);
    }

    /*
    @Override
    public INBT getCompound(String key) {
        return NBTHelper.toCommon(nbt.getCompoundTag(key));
    }*/
}
