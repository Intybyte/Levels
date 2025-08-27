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

    public static final SimpleAttribute SOUL_BOUND;
    public static final SimpleAttribute UNBREAKABLE;

    public static final String ANY_TRANSLATION_KEY = "levels.attributes.any";
    public static final String ANY_BASE_KEY = "levels.any";

    static {

        SimpleAttribute.SimpleAttributeBuilder simpleBuilder = SimpleAttribute.builder()
                .rarity(AttributeRarity.UNCOMMON)
                .allowedTypes(ItemType.values());

        LevelAttribute.LevelAttributeBuilder builder = LevelAttribute.builder()
                .defaultMaxLevel(3);

        FIRE = builder.wrappedAttribute(
                simpleBuilder
                    .baseKey(ANY_BASE_KEY + "fire")
                    .translationKey(ANY_TRANSLATION_KEY + ".fire")
                    .baseName("Fire")
                    .textColor(LegacyTextColor.RED)
                    .build()
                )
                .defaultBaseValue(4)
                .defaultMultiplier(1.25)
                .build();

        FROST = builder.wrappedAttribute(
                simpleBuilder
                    .baseKey(ANY_BASE_KEY + ".frost")
                    .translationKey(ANY_TRANSLATION_KEY + ".frost")
                    .baseName("Frost")
                    .textColor(LegacyTextColor.AQUA)
                    .build()
                )
                .defaultBaseValue(20)
                .defaultMultiplier(1.5)
                .build();

        POISON = builder.wrappedAttribute(
                simpleBuilder
                    .baseKey(ANY_BASE_KEY + ".poison")
                    .translationKey(ANY_TRANSLATION_KEY + ".poison")
                    .baseName("Poison")
                    .textColor(LegacyTextColor.DARK_GREEN)
                    .build()
                )
                .defaultBaseValue(140) // 20 * 7
                .defaultMultiplier(1.5)
                .build();

        DURABLE = builder.wrappedAttribute(
                simpleBuilder
                    .baseKey(ANY_BASE_KEY + ".durable")
                    .translationKey(ANY_TRANSLATION_KEY + ".durable")
                    .baseName("Durable")
                    .textColor(LegacyTextColor.GRAY)
                    .build()
                )
                .defaultBaseValue(1)
                .defaultMultiplier(2)
                .build();

        simpleBuilder = simpleBuilder.rarity(AttributeRarity.RARE);
        SOUL_BOUND = simpleBuilder
                .baseKey(ANY_BASE_KEY + ".soul_bound")
                .translationKey(ANY_TRANSLATION_KEY + ".soul_bound")
                .baseName("Soul Bound")
                .build();

        simpleBuilder = simpleBuilder.rarity(AttributeRarity.LEGENDARY);
        UNBREAKABLE = simpleBuilder
                .baseKey(ANY_BASE_KEY + ".unbreakable")
                .translationKey(ANY_TRANSLATION_KEY + ".unbreakable")
                .baseName("Unbreakable")
                .build();
    }
}
