package com.marcosbarbero.kalah.helper;

import com.marcosbarbero.kalah.dto.StartGameDTO;
import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.utils.GameHelper;

/**
 * @author Marcos Barbero
 */
public class Given {

    public static GameBoard gameBoard(int stones, String gameId) {
        GameBoard board = new GameBoard();
        GameHelper.setup(board, stones);
        board.setId(gameId);
        return board;
    }

    public static StartGameDTO startGame(int stones) {
        return new StartGameDTO(stones);
    }
}
