package com.thexfactor117.levels.common.nbt;

interface BaseINBTGetter<T> {
    boolean hasKey(T key);

    int getInt(T key);

    double getDouble(T key);

    void remove(T key);

    INBT getCompound(T key);
}
