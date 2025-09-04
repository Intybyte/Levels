package com.thexfactor117.levels.common.resources;

import com.thexfactor117.levels.common.config.ConfigEntry;
import com.thexfactor117.levels.common.config.ConfigEntryHolder;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.attributes.display.Formatter;
import com.thexfactor117.levels.common.utils.PropertiesUtil;
import lombok.Getter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Localization implements Formatter, ConfigEntryHolder {
    @Getter
    private static Localization localization;
    private final Map<String, Map<String, String>> localeToTranslation = new HashMap<>();

    private final Path directoryLang;

    public static void init(File file) {
        if (localization == null)
            localization = new Localization(file);
    }

    private Localization(File file) {
        directoryLang = file.toPath().resolve(Paths.get("assets", "levels", "lang"));
        reload();
    }

    public void reload() {
        File[] files = directoryLang.toFile().listFiles();
        if (files == null) {
            System.err.println("Error initializing locale");
            return;
        }

        for (File tlFile : files) {
            String name = tlFile.getName().split("\\.")[0];
            Map<String, String> mapped = PropertiesUtil.loadFile(tlFile);
            localeToTranslation.put(name, mapped);
        }
    }

    @Override
    public String format(String str, String... args) {
        Map<String, String> translator = localeToTranslation.get(Configs.getInstance().main.get("language"));
        String text = translator.get(str);
        if (text == null) {
            System.err.println("Error finding locale");
            return str;
        }

        return String.format(text, (Object[]) args);
    }

    @Override
    public ConfigEntry getEntry() {
        HashMap<String, String> def = new HashMap<>();
        def.put("language", "en_US");

        return new ConfigEntry(
                def
        );
    }
}
