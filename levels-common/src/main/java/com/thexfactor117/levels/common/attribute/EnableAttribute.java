package com.thexfactor117.levels.common.attribute;

import java.util.ArrayList;
import java.util.List;

public interface EnableAttribute {
    boolean isEnabled();

    static <T extends EnableAttribute> List<T> getEnabled(T[] values) {
        List<T> list = new ArrayList<>();

        for (T value : values) {
            if (value.isEnabled()) {
                list.add(value);
            }
        }

        return list;
    }
}
