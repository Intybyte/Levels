package com.thexfactor117.levels.forge.nbt;

import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.nbt.INBTList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.NBTTagList;

@Getter
@AllArgsConstructor
public class NBTListImpl implements INBTList {
    private final NBTTagList nbt;

    @Override
    public boolean hasKey(Integer key) {
        return key < nbt.tagCount();
    }

    @Override
    public int getInt(Integer key) {
        return nbt.getIntAt(key);
    }

    @Override
    public void remove(Integer key) {
        nbt.removeTag(key);
    }

    @Override
    public INBT getCompound(Integer key) {
        return NBTHelper.toCommon(nbt.getCompoundTagAt(key));
    }
}
