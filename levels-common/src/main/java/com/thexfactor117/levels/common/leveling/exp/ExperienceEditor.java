package com.thexfactor117.levels.common.leveling.exp;

import com.thexfactor117.levels.common.config.ConfigManager;
import com.thexfactor117.levels.common.config.Configs;
import com.thexfactor117.levels.common.nbt.INBTHolder;

public interface ExperienceEditor extends INBTHolder {

    void levelUp();

    /**
     * Returns the level of the current weapon/armor.
     * @return level of the item
     */
    default int getLevel() {
        return getNBT() != null ? getNBT().getInt("LEVEL") : 1;
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
            getNBT().setInt("LEVEL", level);
        else
            getNBT().remove("LEVEL");
    }

    default boolean isMaxLevel() {
        return getLevel() >= Configs.getInstance().main.getInt("maxLevel");
    }

    /**
     * Returns the experience of the current weapon/armor.
     * @return experience
     */
    default int getExperience() {
        return getNBT() != null ? getNBT().getInt("EXPERIENCE") : 1;
    }

    /**
     * Sets the experience of the current weapon/armor.
     */
    default void setExperience(int experience) {
        if (getNBT() == null) {
            return;
        }

        if (experience > 0)
            getNBT().setInt("EXPERIENCE", experience);
        else
            getNBT().remove("EXPERIENCE");
    }

    default void addExperience(int experience) {
        setExperience(getExperience() + experience);
    }

    /**
     * Returns how many Attribute Tokens the specific NBT tag has.
     * @return
     */
    default int getAttributeTokens() {
        return getNBT() != null ? getNBT().getInt("TOKENS") : 0;
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
            getNBT().setInt("TOKENS", tokens);
        else
            getNBT().remove("TOKENS");
    }

    default void addAttributeTokens(int toAdd) {
        setAttributeTokens(getAttributeTokens() + toAdd);
    }

    static int getNextLevelExperience(int currentLevel) {
        ConfigManager cfg = Configs.getInstance().main;
        return (int) (Math.pow(currentLevel, cfg.getDouble("expExponent")) * cfg.getDouble("expMultiplier"));
    }
}
