package com.marcosbarbero.kalah.exception;

import com.marcosbarbero.kalah.model.entity.enums.PlayerId;

/**
 * @author Marcos Barbero
 */
public class GameFinishedException extends RuntimeException {

    public GameFinishedException(String gameId, PlayerId playerId, Integer stones) {
        super(String.format("The game with id '%s' is finished and the player '%s' is the winner with '%s' stones.", gameId, playerId.name(), stones.toString()));
    }

    public GameFinishedException() {
        super("There's no winner because the number of stones in the houses is the same.");
    }
}
