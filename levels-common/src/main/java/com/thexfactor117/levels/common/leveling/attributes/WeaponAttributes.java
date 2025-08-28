package com.thexfactor117.levels.common.leveling.attributes;

import com.thexfactor117.levels.common.color.LegacyTextColor;
import com.thexfactor117.levels.common.leveling.ItemType;
import com.thexfactor117.levels.common.leveling.attributes.components.AttributeRarity;

public class WeaponAttributes {
    public static final LevelAttribute CRITICAL;
    public static final LevelAttribute ABSORB;
    public static final LevelAttribute VOID;

    public static final String WEAPON_TRANSLATION_KEY = "levels.attributes.weapon";
    public static final String WEAPON_BASE_KEY = "levels.weapon";

    static {
        SimpleAttribute.SimpleAttributeBuilder simpleBuilder = SimpleAttribute.builder()
                .rarity(AttributeRarity.RARE)
                .allowedTypes(new ItemType[]{ItemType.SWORD, ItemType.BOW});

        LevelAttribute.LevelAttributeBuilder builder = LevelAttribute.builder()
                .defaultMaxLevel(3)
                .defaultMultiplier(1.5);

        CRITICAL = builder.wrappedAttribute(
                        simpleBuilder
                                .baseKey(WEAPON_BASE_KEY + "critical")
                                .translationKey(WEAPON_TRANSLATION_KEY + ".critical")
                                .baseName("Critical")
                                .textColor(LegacyTextColor.BLUE)
                                .build()
                )
                .defaultBaseValue(0.2)
                .build();

        ABSORB = builder.wrappedAttribute(
                        simpleBuilder
                                .baseKey(WEAPON_BASE_KEY + "absorb")
                                .translationKey(WEAPON_TRANSLATION_KEY + ".absorb")
                                .baseName("Absorb")
                                .textColor(LegacyTextColor.GREEN)
                                .build()
                )
                .defaultBaseValue(0.25)
                .build();

        simpleBuilder = simpleBuilder.rarity(AttributeRarity.LEGENDARY);
        VOID = builder.wrappedAttribute(
                        simpleBuilder
                                .baseKey(WEAPON_BASE_KEY + "void")
                                .translationKey(WEAPON_TRANSLATION_KEY + ".void")
                                .baseName("Void")
                                .textColor(LegacyTextColor.DARK_GRAY)
                                .build()
                )
                .defaultBaseValue(15)
                .defaultMultiplier(0.8)
                .build();
    }

}
