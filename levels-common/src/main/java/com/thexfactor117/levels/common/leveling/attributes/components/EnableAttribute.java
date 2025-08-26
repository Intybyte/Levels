package com.thexfactor117.levels.common.leveling.attributes.components;

import java.util.ArrayList;
import java.util.List;

public interface EnableAttribute {
    static <T extends AttributeBase> List<T> getEnabled(T[] values) {
        List<T> list = new ArrayList<>();

        for (T value : values) {
            if (value.isEnabled()) {
                list.add(value);
            }
        }

        return list;
    }
}
