package com.thexfactor117.levels.common.config;

import com.thexfactor117.levels.common.leveling.attributes.AnyAttributes;
import com.thexfactor117.levels.common.leveling.attributes.ArmorAttribute;
import com.thexfactor117.levels.common.leveling.attributes.BowAttribute;
import com.thexfactor117.levels.common.leveling.attributes.SwordAttribute;
import com.thexfactor117.levels.common.leveling.attributes.WeaponAttributes;
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

            instance.attributes
                    .processClazz(ArmorAttribute.class)
                    .processClazz(BowAttribute.class)
                    .processClazz(SwordAttribute.class)
                    .processClazz(AnyAttributes.class)
                    .processClazz(WeaponAttributes.class)
                    .initFile();

            instance.main
                    .process(new MainConfig())
                    .initFile();
        }
    }
}
