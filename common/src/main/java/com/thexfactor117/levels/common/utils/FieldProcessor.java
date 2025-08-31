package com.thexfactor117.levels.common.utils;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldProcessor {

    // Cache for method handles per enum class
    private static final ConcurrentHashMap<Class<? extends Enum<?>>, MethodHandle> enumCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, Map<String, MethodHandle>> cache = new ConcurrentHashMap<>();
    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @SuppressWarnings("unchecked")
    private static <T extends Enum<?>> T[] getValues(Class<T> clazz) {
        try {
            // Try to get cached method handle
            MethodHandle handle = enumCache.computeIfAbsent(clazz, key -> {
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

    /**
     * Analyzes static fields of a specific type
     *
     *
     * @param wanted Class or superclass of fields wanted
     * @param clazz Class to analyze
     * @return field values
     */
    public static <T> List<T> getFields(Class<T> wanted, Class<?> clazz) {
        List<T> list = new ArrayList<>();

        if (clazz.isEnum())  {
            @SuppressWarnings("unchecked")
            Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) clazz;
            for (Enum<?> constant : getValues(enumClass)) {
                if (wanted.isInstance(constant)) {
                    list.add(wanted.cast(constant));
                }
            }
            return list;
        }

        Map<String, MethodHandle> methodCache = cache.computeIfAbsent(clazz, key -> {
            Map<String, MethodHandle> newMap = new HashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers)) {
                    continue;
                }

                final String name = field.getName();
                newMap.computeIfAbsent(name, key2 -> {
                    try {
                        return lookup.findStaticGetter(clazz, name, field.getType());
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            return newMap;
        });

        for (MethodHandle handle : methodCache.values()) {
            Object value;
            try {
                value = handle.invoke();
                if (wanted.isInstance(value)) {
                    list.add(wanted.cast(value));
                }
            } catch (Throwable ignored) {
            }
        }

        return list;
    }

    public static <T> List<T> getFields(Class<T> wanted, Class<?>... clazz) {
        List<T> list = new ArrayList<>();

        for (Class<?> clazzLoop : clazz) {
            list.addAll(getFields(wanted, clazzLoop));
        }

        return list;
    }
}
