package com.thexfactor117.levels.common.config;

import com.thexfactor117.levels.common.utils.FieldProcessor;
import com.thexfactor117.levels.common.utils.PropertiesUtil;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> available = PropertiesUtil.loadFile(file);
        for (Map.Entry<String, String> single : map.entrySet()) {
            if (!available.containsKey(single.getKey())) { // filter out already existing ones
                available.put(single.getKey(), single.getValue());
                modified = true;
            }
        }

        if (modified) PropertiesUtil.saveFile(available, file);
        map = available;
    }

    public void save() {
        PropertiesUtil.saveFile(map, file);
    }

    public void reload() {
        map = PropertiesUtil.loadFile(file);
    }
}
