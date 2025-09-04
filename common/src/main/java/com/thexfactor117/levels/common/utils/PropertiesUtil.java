package com.thexfactor117.levels.common.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
    public static void saveFile(Map<String, String> toSave, File file) {
        Properties properties = new Properties();
        properties.putAll(toSave);
        try (FileWriter writer = new FileWriter(file)) {
            properties.store(writer, "Saved Config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> loadFile(File file) {
        if (!file.exists()) {
            return new HashMap<>();
        }

        Properties properties = new Properties();
        try (FileReader reader = new FileReader(file)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, String> map = new HashMap<>();
        for (String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }

        return map;
    }
}
