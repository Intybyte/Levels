package com.thexfactor117.levels.architectury.nbt;

import com.thexfactor117.levels.common.nbt.INBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;

@Getter
@AllArgsConstructor
public class NBTImpl implements INBT {
    private CompoundTag nbt;

    @Override
    public boolean hasKey(String key) {
        return nbt.contains(key);
    }

    @Override
    public int getInt(String key) {
        return nbt.getInt(key);
    }

    @Override
    public void remove(String key) {
        nbt.remove(key);
    }

    @Override
    public void setInt(String key, int value) {
        nbt.putInt(key, value);
    }
}
