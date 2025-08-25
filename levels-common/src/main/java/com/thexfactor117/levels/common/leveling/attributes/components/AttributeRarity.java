package com.thexfactor117.levels.common.leveling.attributes.components;

import lombok.Getter;

@Getter
public enum AttributeRarity {
    UNCOMMON(1),
    RARE(2),
    LEGENDARY(3),
    ;

    private final int cost;

    AttributeRarity(int cost) {
        this.cost = cost;
    }
}
