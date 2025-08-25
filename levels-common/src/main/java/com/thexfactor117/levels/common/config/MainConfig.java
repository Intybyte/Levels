package com.thexfactor117.levels.common.config;

import java.util.HashMap;

public class MainConfig implements ConfigEntryHolder{

    private final HashMap<String, String> map = new HashMap<>();

    // changing this requires updating the relative tests
    public MainConfig() {
        map.put("maxLevel", "10");
        map.put("expExponent", "2.1");
        map.put("expMultiplier", "20");

        map.put("itemBlackList", "modid:item,modid:item2");
        map.put("unlimitedDurability", "false");
    }

    @Override
    public ConfigEntry getEntry() {
        return new ConfigEntry(map);
    }
}
