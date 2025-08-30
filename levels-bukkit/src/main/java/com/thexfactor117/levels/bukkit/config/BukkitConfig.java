package com.thexfactor117.levels.bukkit.config;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.ConfigEntryHolder;

import java.util.Map;

public class BukkitConfig implements ConfigEntryHolder {
    @Override
    public ConfigEntry getEntry() {
        return new ConfigEntry(
                Map.of("patchCustomItems", "false")
        );
    }
}
