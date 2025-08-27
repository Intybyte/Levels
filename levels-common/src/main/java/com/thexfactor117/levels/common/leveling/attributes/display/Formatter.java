package com.thexfactor117.levels.common.leveling.attributes.display;

@FunctionalInterface
public interface Formatter {
    String format(String str, String... args);
}
