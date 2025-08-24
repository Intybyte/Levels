package com.thexfactor117.levels.common.attribute;

import com.thexfactor117.levels.common.LegacyTextColor;
import lombok.Getter;

@Getter
public enum AttributeRarity {
    UNCOMMON(LegacyTextColor.DARK_GREEN, 0x00AA00, 1, 0xF0100010, 0x5000AA00),
    RARE(LegacyTextColor.AQUA, 0x55FFFF, 2, 0xF0100010, 0x5055FFFF),
    LEGENDARY(LegacyTextColor.DARK_PURPLE, 0xAA00AA, 3, 0xF0100010, 0x50AA00AA),
    ;

    private final String color;
    private final int hex;
    private final int cost;
    private final int backColor;
    private final int borderColor;

    AttributeRarity(Object color, int hex, int cost, int backColor, int borderColor) {
        this.color = color.toString();
        this.hex = hex;
        this.cost = cost;
        this.backColor = backColor;
        this.borderColor = borderColor;
    }
}
