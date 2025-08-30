package com.thexfactor117.levels.common.leveling.attributes.components;

import java.util.concurrent.ThreadLocalRandom;

public interface ChanceBase {
    /**
     * @return Must be a number between 0.0 and 100.0
     */
    double getChance();

    default boolean rollChance() {
        return getChance() >= ThreadLocalRandom.current().nextDouble() * 100;
    }
}
