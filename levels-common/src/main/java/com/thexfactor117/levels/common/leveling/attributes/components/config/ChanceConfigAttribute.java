package com.thexfactor117.levels.common.leveling.attributes.components.config;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.attributes.components.ChanceBase;

public interface ChanceConfigAttribute extends EnableConfigAttribute, ChanceBase {
    double getDefaultChance();

    /**
     * @return Must be a number between 0.0 and 100.0
     */
    default double getChance() {
        return Configs.getInstance().attributes.getDouble(getBaseKey() + ".chance");
    }

    default ConfigEntry getEntry() {
        ConfigEntry cfg = EnableConfigAttribute.super.getEntry();
        cfg.set(getBaseKey() + ".chance", String.valueOf(getDefaultChance()));

        return cfg;
    }
}
