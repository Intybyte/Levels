package com.thexfactor117.levels.common.leveling.attributes.components.config;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.Configs;

import java.util.Map;

public interface SimpleConfigAttribute extends EnableConfigAttribute {

    static String keyOf(Enum<?> enm) {
        return enm.getClass().getSimpleName() + "." +  enm.name().toLowerCase();
    }

    String getBaseKey();

    double getDefaultBaseValue();
    double getDefaultMultiplier();

    default double getBaseValue() {
        return Configs.getInstance().attributes.getDouble(getBaseKey() + ".base_value");
    }

    default double getMultiplier() {
        return Configs.getInstance().attributes.getDouble(getBaseKey() + ".multiplier");
    }

    default ConfigEntry getEntry() {
        Map<String, String> cfg = EnableConfigAttribute.super.getEntry().getMap();
        cfg.put(getBaseKey() + ".base_value", String.valueOf(getDefaultBaseValue()));
        cfg.put(getBaseKey() + ".multiplier", String.valueOf(getDefaultMultiplier()));

        return new ConfigEntry(
                cfg
        );
    }
}
