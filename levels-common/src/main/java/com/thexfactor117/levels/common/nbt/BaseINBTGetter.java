package com.thexfactor117.levels.common.nbt;

interface BaseINBTGetter<T> {
    boolean hasKey(T key);

    /**
     * Retrieves an integer value using the specified key, or 0 if no such key was stored.
     */
    int getInt(T key);

    double getDouble(T key);

    void remove(T key);

    INBT getCompound(T key);
}
