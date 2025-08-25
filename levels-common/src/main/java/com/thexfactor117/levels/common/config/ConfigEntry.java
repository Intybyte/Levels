package com.thexfactor117.levels.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ConfigEntry implements ConfigMap {
    final private Map<String, String> map;

    public ConfigEntry() {
        map = new HashMap<>();
    }
}
