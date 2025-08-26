package com.thexfactor117.levels.common.leveling.attributes.components.config;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.Configs;

import java.util.HashMap;

public interface EnableConfigAttribute extends BaseConfigAttribute {
    default boolean isEnabled() {
        return Configs.getInstance().attributes.getBoolean(getBaseKey() + ".enabled");
    }

    default ConfigEntry getEntry() {
        HashMap<String, String> cfg = new HashMap<>();
        cfg.put(getBaseKey() + ".enabled", "true");

        return new ConfigEntry(
                cfg
        );
    }
}
