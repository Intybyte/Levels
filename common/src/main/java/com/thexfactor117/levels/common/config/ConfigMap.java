package com.thexfactor117.levels.common.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigMap {
    Map<String, String> getMap();

    default void set(String key, String value) {
        getMap().put(key, value);
    }

    default String get(String key) {
        return getMap().get(key);
    }

    default int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    default double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    default boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    default List<String> getStringList(String key) {
        return Arrays.asList(get(key).split(","));
    }

    default Set<String> getStringSet(String key) {
        return new HashSet<>(getStringList(key));
    }
}
