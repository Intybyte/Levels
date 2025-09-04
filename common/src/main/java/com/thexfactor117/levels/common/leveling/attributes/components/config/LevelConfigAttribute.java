package com.thexfactor117.levels.common.leveling.attributes.components.config;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeBase;

public interface LevelConfigAttribute extends EnableConfigAttribute {
    int getDefaultMaxLevel();

    default int getMaxLevel() {
        return Configs.getInstance().attributes.getInt(getBaseKey() + ".max_level");
    }

    default ConfigEntry getEntry() {
        ConfigEntry cfg = EnableConfigAttribute.super.getEntry();
        cfg.set(getBaseKey() + ".max_level", String.valueOf(getDefaultMaxLevel()));

        return cfg;
    }

    static int getMaxLevel(AttributeBase attr) {
        int maxLevel = 1;
        if (attr instanceof LevelConfigAttribute) {
            maxLevel = ((LevelConfigAttribute) attr).getMaxLevel();
        }
        return maxLevel;
    }
}
