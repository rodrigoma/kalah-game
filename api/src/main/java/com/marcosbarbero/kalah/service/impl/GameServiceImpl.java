package com.marcosbarbero.kalah.service.impl;

import com.marcosbarbero.kalah.exception.GameFinishedException;
import com.marcosbarbero.kalah.exception.ResourceNotFoundException;
import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.entity.Player;
import com.marcosbarbero.kalah.model.entity.Spot;
import com.marcosbarbero.kalah.model.entity.enums.PlayerId;
import com.marcosbarbero.kalah.model.entity.enums.SpotId;
import com.marcosbarbero.kalah.model.repository.GameRepository;
import com.marcosbarbero.kalah.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.marcosbarbero.kalah.utils.GameHelper.*;

/**
 * The service layer for the Kalah Game.
 *
 * @author Marcos Barbero
 */
@Service
public class GameServiceImpl implements GameService {

    private static final int LANDED_EMPTY_SPOT = 1;

    private final GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public GameBoard create(int stones) {
        GameBoard board = new GameBoard();
        setup(board, stones);
        return this.gameRepository.save(board);
    }

    @Override
    public GameBoard get(String gameId) {
        return this.gameRepository.findById(gameId);
    }

    @Override
    public GameBoard move(String gameId, SpotId spotId) {
        Optional<GameBoard> gameBoard = Optional.ofNullable(this.gameRepository.findById(gameId));
        gameBoard.orElseThrow(ResourceNotFoundException::new);
        validateMatch(gameBoard.get());
        reorganize(gameBoard.get().getCurrentPlayer(), spotId, gameBoard.get());
        return gameBoard.get();
    }

    private boolean validSpot(Spot spot) {
        return spot.getStones() > ZERO;
    }

    private void validateMatch(GameBoard gameBoard) {
        Player currentPlayer = getPlayer(gameBoard.getCurrentPlayer(), gameBoard);
        int count = countStonesInSpots(currentPlayer, false);
        if (count == ZERO) {
            Player otherPlayer = getOpponentPlayer(gameBoard.getCurrentPlayer(), gameBoard);
            int totalInSpots = countStonesInSpots(otherPlayer, true);
            otherPlayer.getHouse().setStones(otherPlayer.getHouse().getStones() + totalInSpots);

            Integer stones;
            PlayerId playerId;
            if (currentPlayer.getHouse().getStones() > otherPlayer.getHouse().getStones()) {
                playerId = currentPlayer.getId();
                stones = currentPlayer.getHouse().getStones();
            } else {
                playerId = otherPlayer.getId();
                stones = otherPlayer.getHouse().getStones();
            }

            throw new GameFinishedException(gameBoard.getId(), playerId, stones);
        }
    }

    private void reorganize(PlayerId currentPlayerId, SpotId spotId, GameBoard gameBoard) {
        Player currentPlayer = getPlayer(currentPlayerId, gameBoard);
        Spot currentSpot = getSpot(currentPlayer, spotId);
        if (validSpot(currentSpot)) {
            int stones = currentSpot.getStones();
            boolean endedOwnHouse = willEndOwnHouse(stones, spotId);
            currentSpot.setStones(ZERO);
            PlayerId playerId = iterate(gameBoard, gameBoard.getCurrentPlayer(), spots(currentPlayer, spotId), stones);
            if (!endedOwnHouse) {
                landedEmptySpot(playerId, PlayerId.getLandedSpot(), gameBoard);
            }
            validateMatch(gameBoard);
            setCurrentPlayer(gameBoard, endedOwnHouse);
        }
    }

    /*
     * When the last stone lands in an own empty pit, the player captures this stone and all stones in
     * the opposite pit (the other players' pit) and puts them in his own Kalah.
     */
    private void landedEmptySpot(PlayerId playerId, SpotId spotId, GameBoard gameBoard) {
        if (gameBoard.getCurrentPlayer().equals(playerId)) {
            Player player = getPlayer(playerId, gameBoard);
            Spot spot = getSpot(player, spotId);
            if (spot.getStones() == LANDED_EMPTY_SPOT) {
                Player otherPlayer = getOpponentPlayer(playerId, gameBoard);
                Spot oppositeSpot = getSpot(otherPlayer, getOppositeSpotId(spotId));
                player.getHouse().setStones(player.getHouse().getStones() + oppositeSpot.getStones() + spot.getStones());
                oppositeSpot.setStones(ZERO);
                spot.setStones(ZERO);
            }
        }
    }

    private PlayerId iterate(GameBoard gameBoard, PlayerId playerId, List<Spot> spots, int stones) {
        for (Spot spot : spots) {
            if (stones == ZERO) {
                break;
            }
            spot.setStones(spot.getStones() + 1);
            stones--;
            PlayerId.setLandedSpot(spot.getId());
        }

        if (stones > ZERO && playerId.equals(gameBoard.getCurrentPlayer())) {
            Player player = getPlayer(playerId, gameBoard);
            player.getHouse().setStones(player.getHouse().getStones() + 1);
            stones--;
        }

        if (stones > ZERO) {
            Player player = getOpponentPlayer(playerId, gameBoard);
            playerId = iterate(gameBoard, player.getId(), player.getSpots(), stones);
        }
        return playerId;
    }

}
