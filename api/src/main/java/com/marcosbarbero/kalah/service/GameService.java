package com.marcosbarbero.kalah.service;

import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.entity.enums.SpotId;

/**
 * Game services.
 *
 * @author Marcos Barbero
 */
public interface GameService {

    /**
     * Create a new game.
     *
     * @param stones The number os stones per spot
     * @return GameBoard
     */
    GameBoard create(int stones);

    /**
     * Return the GameBoard for the given uuid.
     *
     * @param gameId The gameId
     * @return The GameBoard
     */
    GameBoard get(String gameId);

    /**
     * Return the GameBoard for the given id.
     *
     * @param gameId The gameId
     * @param spotId The chosen spot id
     * @return The GameBoard
     */
    GameBoard move(String gameId, SpotId spotId);
}
