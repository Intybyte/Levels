package com.thexfactor117.levels.common.leveling.exp;

import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.nbt.INBTHolder;
import com.thexfactor117.levels.common.nbt.INBTList;
import com.thexfactor117.levels.common.nbt.NBTType;

public interface LevelUpProcessor extends INBTHolder {

    ExperienceEditor getExpEditor();

    void notifyLevelUp();

    boolean isWeapon();

    boolean isArmor();

    void saveLevelUp();

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

            double multiplier = nbt.getDouble("Multiplier");
            INBTList taglist = nbt.getList("AttributeModifiers", NBTType.COMPOUND); // retrieves our custom Attribute Modifier implementation
            if (isWeapon()) {
                // update damage and attack speed values
                INBT damageNbt = taglist.getCompound(0);
                INBT speedNbt = taglist.getCompound(1);

                double damageAmount = damageNbt.getDouble("Amount");
                double speedAmount = speedNbt.getDouble("Amount");

                double newDamage = damageAmount + ((damageAmount * multiplier) / 2);
                double newSpeed = speedAmount - ((speedAmount * multiplier) / 2);

                damageNbt.setDouble("Amount", newDamage);
                speedNbt.setDouble("Amount", newSpeed);
            } else if (isArmor()) {
                // update armor and armor toughness values
                INBT armorNbt = taglist.getCompound(0);
                INBT toughnessNbt = taglist.getCompound(1);

                double armorAmount = armorNbt.getDouble("Amount");
                double toughnessAmount = toughnessNbt.getDouble("Amount");

                double newArmor = armorAmount + ((armorAmount * multiplier) / 2);
                double newToughness = toughnessAmount - ((toughnessAmount * multiplier) / 2);
                armorNbt.setDouble("Amount", newArmor);
                toughnessNbt.setDouble("Amount", newToughness);
            }

            saveLevelUp();
        }
    }
}
