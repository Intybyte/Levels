package com.thexfactor117.levels.common.nbt;

public interface INBT {
    boolean hasKey(String key);

    int getInt(String key);

    void setInt(String key, int value);

    void remove(String key);
}
