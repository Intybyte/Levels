package com.thexfactor117.levels.common.leveling.attributes;


import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;

/**
 * Contains attributes that can be applied to both armor and weapons alike
 */
public class AnyAttributes {
    public static final LevelAttribute FIRE;
    public static final LevelAttribute FROST;
    public static final LevelAttribute POISON;
    public static final LevelAttribute DURABLE;

    static {
        String translateKey = "levels.attributes.any";

        LevelAttribute.LevelAttributeBuilder builder = LevelAttribute.builder()
                .defaultMaxLevel(3)
                .rarity(AttributeRarity.UNCOMMON)
                .allowedTypes(ItemType.values());

        FIRE = builder
                .baseKey("FIRE")
                .translationKey(translateKey + ".fire")
                .baseName("Fire")
                .color(LegacyTextColor.RED)
                .defaultBaseValue(4)
                .defaultMultiplier(1.25)
                .build();

        FROST = builder
                .baseKey("FROST")
                .translationKey(translateKey + ".frost")
                .baseName("Frost")
                .color(LegacyTextColor.AQUA)
                .defaultBaseValue(20)
                .defaultMultiplier(1.5)
                .build();

        POISON = builder
                .baseKey("POISON")
                .translationKey(translateKey + ".poison")
                .baseName("Poison")
                .color(LegacyTextColor.DARK_GREEN)
                .defaultBaseValue(140) // 20 * 7
                .defaultMultiplier(1.5)
                .build();

        DURABLE = builder
                .baseKey("DURABLE")
                .translationKey(translateKey + ".durable")
                .baseName("Durable")
                .color(LegacyTextColor.GRAY)
                .defaultBaseValue(1)
                .defaultMultiplier(2)
                .build();
    }
}
