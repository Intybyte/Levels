package com.thexfactor117.levels.common.leveling.exp;

import com.thexfactor117.levels.common.leveling.SavingEditor;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.nbt.INBTHolder;

public interface LevelUpProcessor extends INBTHolder, SavingEditor {

    ExperienceEditor getExpEditor();

    void notifyLevelUp();

    boolean isWeapon();

    void levelUpWeapon();

    boolean isArmor();

    void levelUpArmor();

    /**
     * Levels up the current weapon/armor to the next level, assuming it is not at max level.
     */
    default void levelUp() {
        INBT nbt = getNBT();
        ExperienceEditor editor = getExpEditor();

        if (nbt == null) {
            return;
        }

        while (!editor.isMaxLevel() && editor.getExperience() >= ExperienceEditor.getNextLevelExperience(editor.getLevel())) {
            editor.setLevel(editor.getLevel() + 1); // increase level by one
            editor.addAttributeTokens(1);

            notifyLevelUp();

            if (isWeapon()) {
                levelUpWeapon();
            } else if (isArmor()) {
                levelUpArmor();
            }

            saveEdits();
        }
    }
}
