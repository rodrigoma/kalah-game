package com.marcosbarbero.kalah.model.entity.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumerator representing the spot ids for the players.
 *
 * @author Marcos Barbero
 */
public enum SpotId {

    SPOT_1,
    SPOT_2,
    SPOT_3,
    SPOT_4,
    SPOT_5,
    SPOT_6;

    static Map<Integer, SpotId> lookup = new HashMap<>();

    static {
        for (SpotId spotId : SpotId.values()) {
            lookup.put(spotId.ordinal(), spotId);
        }
    }

    public static SpotId getByOrdinal(int ordinal) {
        return lookup.get(ordinal);
    }
}
