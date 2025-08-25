package com.thexfactor117.levels.common.leveling.exp;

import com.thexfactor117.levels.common.config.ConfigManager;
import com.thexfactor117.levels.common.config.Configs;

public interface ExperienceEditor {

    void levelUp();

    int getLevel();

    void setLevel(int level);

    default boolean isMaxLevel() {
        return getLevel() >= Configs.getInstance().main.getInt("maxLevel");
    }

    int getExperience();

    void setExperience(int experience);

    default void addExperience(int experience) {
        setExperience(getExperience() + experience);
    }

    int getAttributeTokens();

    void setAttributeTokens(int tokens);

    default void addAttributeTokens(int toAdd) {
        setAttributeTokens(getAttributeTokens() + toAdd);
    }

    static int getNextLevelExperience(int currentLevel) {
        ConfigManager cfg = Configs.getInstance().main;
        return (int) (Math.pow(currentLevel, cfg.getDouble("expExponent")) * cfg.getDouble("expMultiplier"));
    }
}
