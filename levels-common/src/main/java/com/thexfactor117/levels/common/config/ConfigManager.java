package com.thexfactor117.levels.common.config;

import com.thexfactor117.levels.common.utils.FieldProcessor;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
public class ConfigManager implements ConfigMap {
    private final File file;
    private Map<String, String> map = new HashMap<>();

    public ConfigManager(File file) {
        this.file = file;
    }

    @SafeVarargs
    public final <T extends ConfigEntryHolder> ConfigManager process(T... args) {
        for (T arg : args) {
            map.putAll(arg.getEntry().getMap());
        }

        return this;
    }

    public final <T extends ConfigEntryHolder> ConfigManager process(Collection<T> args) {
        for (T arg : args) {
            map.putAll(arg.getEntry().getMap());
        }

        return this;
    }

    public final <T> ConfigManager processClazz(Class<T> clazz) {
        process(
                FieldProcessor.getFields(ConfigEntryHolder.class, clazz)
        );
        return this;
    }

    public void initFile() {
        file.getParentFile().mkdirs();

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean modified = false;
        Map<String, String> available = loadFile();
        for (Map.Entry<String, String> single : map.entrySet()) {
            if (!available.containsKey(single.getKey())) { // filter out already existing ones
                available.put(single.getKey(), single.getValue());
                modified = true;
            }
        }

        if (modified) saveFile(available);
        map = available;
    }

    public void save() {
        saveFile(map);
    }

    public void reload() {
        map = loadFile();
    }

    private void saveFile(Map<String, String> toSave) {
        Properties properties = new Properties();
        properties.putAll(toSave);
        try (FileWriter writer = new FileWriter(file)) {
            properties.store(writer, "Saved Config");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> loadFile() {
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
