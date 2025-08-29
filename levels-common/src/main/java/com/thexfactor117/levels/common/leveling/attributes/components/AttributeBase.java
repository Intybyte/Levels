package com.thexfactor117.levels.common.leveling.attributes.components;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.config.LevelConfigAttribute;
import com.thexfactor117.levels.common.leveling.attributes.components.config.SimpleConfigAttribute;
import com.thexfactor117.levels.common.leveling.attributes.display.Formatter;
import com.thexfactor117.levels.common.leveling.attributes.display.RomanNumeralDisplay;
import com.thexfactor117.levels.common.nbt.INBT;
import com.thexfactor117.levels.common.utils.RomanNumber;

import java.util.ArrayList;
import java.util.List;

public interface AttributeBase extends RomanNumeralDisplay {

    /**
     * Returns true if the NBT tag compound has the specified Attribute.
     * @param nbt
     * @return
     */
    default boolean hasAttribute(INBT nbt) {
        return nbt != null && nbt.hasKey(getAttributeKey());
    }

    /**
     * Adds the specified Attribute to the NBT tag compound.
     * @param nbt
     */
    default void addAttribute(INBT nbt) {
        if (nbt != null) {
            nbt.setInt(getAttributeKey(), 1);
        }
    }

    /**
     * Removes the specified Attribute from the NBT tag compound.
     * @param nbt
     */
    default void removeAttribute(INBT nbt) {
        if (nbt != null) {
            nbt.remove(getAttributeKey());
        }
    }

    /**
     * Sets the tier of the specific attribute.
     * @param nbt
     * @param tier
     */
    default void setAttributeTier(INBT nbt, int tier) {
        if (nbt != null) {
            nbt.setInt(getAttributeKey(), tier);
        }
    }

    /**
     * Returns the tier of the specific attribute.
     * @param nbt
     * @return
     */
    default int getAttributeTier(INBT nbt) {
        return nbt != null ? nbt.getInt(getAttributeKey()) : 0;
    }

    default String getAttributeKey() {
        return "levels:" + getBaseName().toLowerCase().replace(' ', '_') + "_tier";
    }

    AttributeRarity getRarity();

    default String getName(INBT nbt) {
        return getCompleteName(getAttributeTier(nbt));
    }

    boolean isEnabled();

    String getTranslationKey();

    ItemType[] getAllowedTypes();

    default List<String> getUpgradeSummary(INBT nbt, Formatter formatter) {
        int maxLevel = LevelConfigAttribute.getMaxLevel(this);

        int cost = 1;
        if (!this.hasAttribute(nbt)) {
            cost = this.getRarity().getCost();
        }

        if (this.getAttributeTier(nbt) >= maxLevel) cost = 0;

        String translate = this.getTranslationKey();

        List<String> list = new ArrayList<>();
        list.add(this.getColor() + this.getName(nbt));
        list.add(LegacyTextColor.GRAY + "Cost: " + cost + " token(s)");
        list.add("");
        list.add(formatter.format(translate));
        list.add("");
        list.add("Tiers:");

        for (int level = 1; level <= maxLevel; level++) {
            String rmn = RomanNumber.toRoman(level);

            double value = 0;
            if (this instanceof SimpleConfigAttribute) {
                value = ((SimpleConfigAttribute) this).getCalculatedValue(level);
            }

            String tierTranslationKey = String.format("%s.tier", translate);
            if (formatter.format(tierTranslationKey).contains("%s%%")) {
                value *= 100;
            }

            String displayDouble = Math.abs(value % 1) > 0.01
                    ? String.format("%.1f", value)
                    : String.format("%.0f", value);

            String localized = formatter.format(tierTranslationKey, displayDouble);

            list.add(" " + rmn + " - " + this.getColor() + localized);
        }

        return list;
    }
}