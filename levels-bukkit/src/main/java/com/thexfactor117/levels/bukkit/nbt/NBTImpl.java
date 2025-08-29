package com.thexfactor117.levels.bukkit.nbt;

import com.thexfactor117.levels.common.nbt.INBT;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

@Getter
@AllArgsConstructor
public class NBTImpl implements INBT {
    private final PersistentDataContainer pdc;

    private NamespacedKey key(String key) {
        NamespacedKey result = NamespacedKey.fromString(key);
        if (result == null) {
            throw new IllegalArgumentException("Invalid key: " + key);
        }

        return result;
    }

    @Override
    public boolean hasKey(String key) {
        return pdc.has(key(key), PersistentDataType.INTEGER);
    }

    @Override
    public int getInt(String key) {
        Integer value = pdc.get(key(key), PersistentDataType.INTEGER);
        return  value == null ? 0 : value;
    }

    @Override
    public void setInt(String key, int value) {
        pdc.set(key(key), PersistentDataType.INTEGER, value);
    }

    @Override
    public void remove(String key) {
        pdc.remove(key(key));
    }
}
