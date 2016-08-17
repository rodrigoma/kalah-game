package com.marcosbarbero.kalah.model.repository;

import com.marcosbarbero.kalah.model.entity.GameBoard;

/**
 * The GameBoard repository.
 *
 * @author Marcos Barbero
 */
public interface GameRepository {

    /**
     * Find a GameBoard by id.
     *
     * @param gameId The gameId
     * @return The GameBoard
     */
    GameBoard findById(String gameId);

    /**
     * Save a GameBoard.
     *
     * @param board The GameBoard
     * @return The saved GameBoardi
     */
    GameBoard save(GameBoard board);
}
