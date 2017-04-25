package com.olts.discipline.api;

import java.util.UUID;

/**
 * o.tsoy
 * 25.04.2017
 */
public class GlobalIdGenerator {

    public static long generateId() {
        return Math.abs(UUID.randomUUID().getLeastSignificantBits());
    }
}
