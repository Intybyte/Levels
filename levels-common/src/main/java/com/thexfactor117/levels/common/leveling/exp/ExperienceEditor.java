package com.thexfactor117.levels.common.leveling.exp;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.config.ConfigManager;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.leveling.attributes.display.Formatter;
import com.thexfactor117.levels.common.nbt.INBTHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * This is just an editor, don't forget to save the INBT changes
 */
public interface ExperienceEditor extends INBTHolder {

    String LEVEL_KEY = "levels:level"; 
    String EXPERIENCE_KEY = "levels:experience";
    String TOKENS_KEY = "levels:tokens";
    
    /**
     * Returns the level of the current weapon/armor.
     * @return level of the item
     */
    default int getLevel() {
        return getNBT() != null ? getNBT().getInt(LEVEL_KEY) : 1;
    }

    /**
     * Sets the level of the current weapon/armor.
     * @param level new level
     */
    default void setLevel(int level) {
        if (getNBT() == null) {
            return;
        }

        if (level > 0)
            getNBT().setInt(LEVEL_KEY, level);
        else
            getNBT().remove(LEVEL_KEY);
    }

    default boolean isMaxLevel() {
        return getLevel() >= Configs.getInstance().main.getInt("maxLevel");
    }

    /**
     * Returns the experience of the current weapon/armor.
     * @return experience
     */
    default int getExperience() {
        return getNBT() != null ? getNBT().getInt(EXPERIENCE_KEY) : 1;
    }

    /**
     * Sets the experience of the current weapon/armor.
     */
    default void setExperience(int experience) {
        if (getNBT() == null) {
            return;
        }

        if (experience > 0)
            getNBT().setInt(EXPERIENCE_KEY, experience);
        else
            getNBT().remove(EXPERIENCE_KEY);
    }

    default void addExperience(int experience) {
        setExperience(getExperience() + experience);
    }

    /**
     * Returns how many Attribute Tokens the specific NBT tag has.
     * @return
     */
    default int getAttributeTokens() {
        return getNBT() != null ? getNBT().getInt(TOKENS_KEY) : 0;
    }

    /**
     * Sets the amount of Attribute Tokens the specific NBT tag has.
     * @param tokens
     */
    default void setAttributeTokens(int tokens) {
        if (getNBT() == null) {
            return;
        }

        if (tokens > 0)
            getNBT().setInt(TOKENS_KEY, tokens);
        else
            getNBT().remove(TOKENS_KEY);
    }

    default void addAttributeTokens(int toAdd) {
        setAttributeTokens(getAttributeTokens() + toAdd);
    }

    static int getNextLevelExperience(int currentLevel) {
        ConfigManager cfg = Configs.getInstance().main;
        return (int) (Math.pow(currentLevel, cfg.getDouble("expExponent")) * cfg.getDouble("expMultiplier"));
    }

    default List<String> displayExp(Formatter formatter) {
        List<String> display = new ArrayList<>();
        int level = this.getLevel();
        // level & experience
        if (this.isMaxLevel()) {
            display.add(LegacyTextColor.GRAY + formatter.format("levels.misc.level") + ": " + formatter.format("levels.misc.max")); // max level
            display.add(LegacyTextColor.GRAY + formatter.format("levels.misc.experience") + ": " + formatter.format("levels.misc.max"));
        } else {
            display.add(LegacyTextColor.GRAY + formatter.format("levels.misc.level") + ": " + level); // level
            display.add(LegacyTextColor.GRAY + formatter.format("levels.misc.experience") + ": " + this.getExperience() + " / " + ExperienceEditor.getNextLevelExperience(level));
        }

        return display;
    }
}
