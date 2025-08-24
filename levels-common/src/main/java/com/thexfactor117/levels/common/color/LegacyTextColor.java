package com.thexfactor117.levels.common.color;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LegacyTextColor {
    BLACK('0', 0x000000),
    DARK_BLUE('1', 0x0000AA),
    DARK_GREEN('2', 0x00AA00),
    DARK_AQUA('3', 0x00AAAA),
    DARK_RED('4', 0xAA0000),
    DARK_PURPLE('5', 0xAA00AA),
    GOLD('6', 0xFFAA00),
    GRAY('7', 0xAAAAAA),
    DARK_GRAY('8', 0x555555),
    BLUE('9', 0x5555FF),
    GREEN('a', 0x55FF55),
    AQUA('b', 0x55FFFF),
    RED('c', 0xFF5555),
    LIGHT_PURPLE('d', 0xFF55FF),
    YELLOW('e', 0xFFFF55),
    WHITE('f', 0xFFFFFF);


    @Getter
    @AllArgsConstructor
    public enum Style {
        OBFUSCATED('k'),
        BOLD('l'),
        STRIKETHROUGH('m'),
        UNDERLINE('n'),
        ITALIC('o'),
        RESET('r');

        private final char code;

        @Override
        public String toString() {
            return "§" + code;
        }
    }

    private final char code;
    private final int hex;

    @Override
    public String toString() {
        return "§" + code;
    }
}
