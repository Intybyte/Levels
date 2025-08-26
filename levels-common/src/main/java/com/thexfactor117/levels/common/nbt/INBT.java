package com.thexfactor117.levels.common.nbt;

public interface INBT extends BaseINBTGetter<String>, BaseINBTSetter<String> {
    INBTList getList(String key, NBTType type);
}
