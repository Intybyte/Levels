package com.thexfactor117.levels.forge.util;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author TheXFactor117
 *
 */
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private double total = 0;

    public void add(double weight, E result) {
        if (weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public E next(Random random) {
        if (total == 0) {
            throw new RuntimeException("Empty random");
        }

        double value = random.nextDouble() * total;
        return map.ceilingEntry(value).getValue();
    }
}