package com.marcosbarbero.kalah.model.entity.enums;

/**
 * The player id representation.
 *
 * @author Marcos Barbero
 */
public enum PlayerId {

    /**
     * The first player id.
     */
    PLAYER_1,

    /**
     * The second player id.
     */
    PLAYER_2;


    private static SpotId landedSpot;

    public static SpotId getLandedSpot() {
        return landedSpot;
    }

    public static void setLandedSpot(SpotId landedSpot) {
        PlayerId.landedSpot = landedSpot;
    }
}
