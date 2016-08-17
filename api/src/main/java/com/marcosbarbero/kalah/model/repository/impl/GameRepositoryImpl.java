package com.marcosbarbero.kalah.model.repository.impl;

import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.repository.GameRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcos Barbero
 */
@Repository
public class GameRepositoryImpl implements GameRepository {

    /*
     * This Map will act like a persistent repository to store the GameBoards objects.
     */
    private static final Map<String, GameBoard> currentGames = new ConcurrentHashMap<>();

    @Override
    public GameBoard findById(String gameId) {
        return currentGames.get(gameId);
    }

    @Override
    public GameBoard save(GameBoard board) {
        String gameId = UUID.randomUUID().toString();
        board.setId(gameId);
        currentGames.put(gameId, board);
        return board;
    }
}
