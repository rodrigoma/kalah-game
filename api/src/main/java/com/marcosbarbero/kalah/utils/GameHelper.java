package com.marcosbarbero.kalah.utils;

import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.entity.House;
import com.marcosbarbero.kalah.model.entity.Player;
import com.marcosbarbero.kalah.model.entity.Spot;
import com.marcosbarbero.kalah.model.entity.enums.PlayerId;
import com.marcosbarbero.kalah.model.entity.enums.SpotId;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class with helper methods to play the game.
 *
 * @author Marcos Barbero
 */
public class GameHelper {

    public static final int ZERO = 0;
    private static final int NUMBER_OF_SPOTS = SpotId.values().length;
    private static final int MAGIC_NUMBER = 11;

    /**
     * Set the current player to the given GameBoard
     *
     * @param gameBoard     The GameBoard
     * @param endedOwnHouse Flag that indicates if the move ended on current player own house
     */
    public static void setCurrentPlayer(GameBoard gameBoard, boolean endedOwnHouse) {
        if (!endedOwnHouse) {
            if (gameBoard.getCurrentPlayer() == null || gameBoard.getCurrentPlayer().equals(PlayerId.PLAYER_2)) {
                gameBoard.setCurrentPlayer(PlayerId.PLAYER_1);
            } else {
                gameBoard.setCurrentPlayer(PlayerId.PLAYER_2);
            }
        }
    }

    /**
     * Calculate if the method will end on the player own house based on start spot and it's stones.
     *
     * @param stones      The number os stones in the spot
     * @param starterSpot The start spot
     * @return boolean
     */
    public static boolean willEndOwnHouse(int stones, SpotId starterSpot) {
        /*
            1  2  3  4  5  6
            7  8  9  10 11
            12 13 14 15 16 17
            18 19 20 21 22
            23 24 25 26 27 28
            29 30 31 32 33
            34 35 36 37 38 39

            0  0  1  2  3  4
            5  6  7  8  9
            10 11 12 13 14 15
         */
        if (stones > NUMBER_OF_SPOTS) {
            while (stones > NUMBER_OF_SPOTS) {
                stones -= MAGIC_NUMBER;
            }
        }
        return starterSpot.ordinal() + stones == NUMBER_OF_SPOTS;
    }

    /**
     * Return the Spot from the given player for the given SpotId.
     *
     * @param player The Player
     * @param spotId The SpotId
     * @return The Spot
     */
    public static Spot getSpot(Player player, SpotId spotId) {
        return player.getSpots().get(spotId.ordinal());
    }

    /**
     * Get the opposite spotId for the given id.
     *
     * @param spotId The SpotId
     * @return Opposite SpotId
     */
    public static SpotId getOppositeSpotId(SpotId spotId) {
        Map<SpotId, SpotId> map = new HashMap<>();
        map.put(SpotId.SPOT_1, SpotId.SPOT_6);
        map.put(SpotId.SPOT_2, SpotId.SPOT_5);
        map.put(SpotId.SPOT_3, SpotId.SPOT_4);
        map.put(SpotId.SPOT_4, SpotId.SPOT_3);
        map.put(SpotId.SPOT_5, SpotId.SPOT_2);
        map.put(SpotId.SPOT_6, SpotId.SPOT_1);
        return map.get(spotId);
    }

    /**
     * Return the Player for the given id and GameBoard
     *
     * @param playerId The PlayerId
     * @param board    The GameBoard
     * @return Player
     */
    public static Player getPlayer(PlayerId playerId, GameBoard board) {
        Player player;
        if (playerId == PlayerId.PLAYER_1) {
            player = board.getFirstPlayer();
        } else {
            player = board.getSecondPlayer();
        }
        return player;
    }

    /**
     * Return the opponent of given PlayerId from the GameBoard
     *
     * @param currentPlayerId The current PlayerId
     * @param board           The GameBoard
     * @return Opponent Player
     */
    public static Player getOpponentPlayer(PlayerId currentPlayerId, GameBoard board) {
        Player player;
        if (currentPlayerId != PlayerId.PLAYER_1) {
            player = board.getFirstPlayer();
        } else {
            player = board.getSecondPlayer();
        }
        return player;
    }

    /**
     * Count the number of stones in all spots for the given player and empty them if necessary.
     *
     * @param player     The Player
     * @param clearSpots Flag that indicates if the spots should be cleared
     * @return The number of stones in the spots
     */
    public static int countStonesInSpots(Player player, boolean clearSpots) {
        int count = 0;
        for (Spot spot : player.getSpots()) {
            count += spot.getStones();
            if (clearSpots) {
                spot.setStones(ZERO);
            }
        }
        return count;
    }

    /**
     * Setup a GameBoard with given number os stones.
     *
     * @param board  The GameBoard
     * @param stones The number of stones
     */
    public static void setup(GameBoard board, int stones) {
        board.setFirstPlayer(setupPlayer(PlayerId.PLAYER_1, stones));
        board.setSecondPlayer(setupPlayer(PlayerId.PLAYER_2, stones));
        setCurrentPlayer(board, false);
    }

    /**
     * Return a list of Spot of a Player starting by the given SpotId.
     *
     * @param player The Player
     * @param spotId The start spotId
     * @return A list of Spot
     */
    public static List<Spot> spots(Player player, SpotId spotId) {
        return player.getSpots().subList(spotId.ordinal() + 1, NUMBER_OF_SPOTS);
    }

    private static Player setupPlayer(PlayerId playerId, int stones) {
        Player player = new Player();
        player.setId(playerId);
        player.setHouse(new House());
        player.setSpots(spots(stones));
        return player;
    }

    private static List<Spot> spots(int stones) {
        List<Spot> spots = new LinkedList<>();
        for (int i = 0; i < NUMBER_OF_SPOTS; i++) {
            spots.add(i, new Spot(SpotId.values()[i], stones));
        }
        return spots;
    }
}
