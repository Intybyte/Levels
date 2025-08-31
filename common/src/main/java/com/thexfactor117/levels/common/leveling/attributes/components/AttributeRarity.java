package com.thexfactor117.levels.common.leveling.attributes.components;

import lombok.Getter;

@Getter
public enum AttributeRarity {
    UNCOMMON(1),
    RARE(2),
    LEGENDARY(3),
    ;

    /**
     * Represents initial cost of the attribute,
     * after unlocking it for the first time
     * all the upgrades cost 1 token
     */
    private final int cost;

    AttributeRarity(int cost) {
        this.cost = cost;
    }
}
