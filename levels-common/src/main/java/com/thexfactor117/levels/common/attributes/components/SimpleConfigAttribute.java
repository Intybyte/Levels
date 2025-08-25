package com.thexfactor117.levels.common.attributes.components;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.ConfigEntryHolder;
import com.thexfactor117.levels.common.config.Configs;

import java.util.HashMap;

public interface SimpleConfigAttribute extends ConfigEntryHolder {

    static String keyOf(Enum<?> enm) {
        return enm.getClass().getSimpleName() + "." +  enm.name().toLowerCase() + ".enabled";
    }

    String getEnabledKey();

    default boolean isEnabled() {
        return Configs.getInstance().attributes.getBoolean(getEnabledKey());
    }

    default ConfigEntry getEntry() {
        HashMap<String, String> cfg = new HashMap<>();
        cfg.put(getEnabledKey(), "true");

        return new ConfigEntry(
                cfg
        );
    }
}
