package com.thexfactor117.levels.common.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;

public class EnumProcessor {

    // Cache for method handles per enum class
    private static final ConcurrentHashMap<Class<? extends Enum<?>>, MethodHandle> cache = new ConcurrentHashMap<>();
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T[] getValues(Class<T> clazz) {
        try {
            // Try to get cached method handle
            MethodHandle handle = cache.computeIfAbsent(clazz, key -> {
                try {
                    Class<?> arrayClazz = Array.newInstance(key, 0).getClass();

                    return lookup.findStatic(key, "values", MethodType.methodType(arrayClazz));
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    throw new RuntimeException("Unable to find 'values' method for enum class: " + key.getName(), e);
                }
            });

            return (T[]) handle.invoke();
        } catch (Throwable t) {
            throw new RuntimeException("Failed to invoke 'values' method on enum class: " + clazz.getName(), t);
        }
    }
}
