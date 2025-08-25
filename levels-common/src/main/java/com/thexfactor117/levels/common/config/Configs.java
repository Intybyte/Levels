package com.thexfactor117.levels.common.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
public class Configs {
    public final ConfigManager main;
    public final ConfigManager attributes;

    private Configs(File parentDir) {
        main = new ConfigManager(new File(parentDir.getPath(), "levels.cfg"));
        attributes = new ConfigManager(new File(parentDir.getPath(), "levels_attributes.cfg"));
    }

    @Getter
    private static Configs instance = null;
    public static void init(File file) {
        if (instance == null) {
            instance = new Configs(file);
        }
    }
}
