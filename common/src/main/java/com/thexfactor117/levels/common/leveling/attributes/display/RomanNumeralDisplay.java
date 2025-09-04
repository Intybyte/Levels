package com.thexfactor117.levels.common.leveling.attributes.display;

import com.thexfactor117.levels.common.utils.RomanNumber;

public interface RomanNumeralDisplay {
    String getBaseName();
    default String getCompleteName(int level) {
        if (level == 0) return getBaseName();

        return getBaseName() + " " + RomanNumber.toRoman(level);
    }

    int getHexColor();
    String getColor();
}
