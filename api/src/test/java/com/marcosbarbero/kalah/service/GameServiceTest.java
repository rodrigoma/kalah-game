package com.marcosbarbero.kalah.service;

import com.marcosbarbero.kalah.exception.ResourceNotFoundException;
import com.marcosbarbero.kalah.helper.Given;
import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.entity.enums.PlayerId;
import com.marcosbarbero.kalah.model.entity.enums.SpotId;
import com.marcosbarbero.kalah.model.repository.GameRepository;
import com.marcosbarbero.kalah.service.impl.GameServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * @author Marcos Barbero
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameServiceTest {

    @MockBean
    private GameRepository gameRepository;

    private GameService gameService;

    @Before
    public void setUp() {
        this.gameService = new GameServiceImpl(this.gameRepository);
    }

    @Test
    public void testCreate_shouldNotReturnError() {
        int stones = 6;
        GameBoard gameBoard = Given.gameBoard(stones, UUID.randomUUID().toString());
        when(this.gameRepository.save(any(GameBoard.class))).thenReturn(gameBoard);
        GameBoard savedBoard = this.gameService.create(stones);
        Assert.notNull(savedBoard);
        Assert.isTrue(gameBoard.getId().equals(savedBoard.getId()));
    }

    @Test
    public void testGet_shouldNotReturnNull() {
        int stones = 6;
        String gameId = UUID.randomUUID().toString();
        GameBoard gameBoard = Given.gameBoard(stones, gameId);
        when(this.gameRepository.findById(gameId)).thenReturn(gameBoard);
        GameBoard found = this.gameService.get(gameId);
        Assert.notNull(found);
        Assert.isTrue(gameBoard.getId().equals(found.getId()));
    }

    @Test
    public void testGet_shouldReturnNull() {
        when(this.gameRepository.findById(any(String.class))).thenReturn(null);
        GameBoard found = this.gameService.get(UUID.randomUUID().toString());
        Assert.isNull(found);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testMove_shouldReturnError() {
        when(this.gameRepository.findById(any(String.class))).thenReturn(null);
        this.gameService.move(UUID.randomUUID().toString(), SpotId.SPOT_1);
    }

    @Test
    public void testMove_finishOwnHouse_shouldReturnSamePlayer() {
        int stones = 6;
        String gameId = UUID.randomUUID().toString();
        GameBoard gameBoard = Given.gameBoard(stones, gameId);
        when(this.gameRepository.findById(gameId)).thenReturn(gameBoard);
        GameBoard moved = this.gameService.move(gameId, SpotId.SPOT_1);
        Assert.isTrue(moved.getCurrentPlayer().equals(gameBoard.getCurrentPlayer()));
    }

    @Test
    public void testMove_finishOnOpponentSpot_shouldReturnOpponentTurn() {
        int stones = 6;
        String gameId = UUID.randomUUID().toString();
        GameBoard gameBoard = Given.gameBoard(stones, gameId);
        when(this.gameRepository.findById(gameId)).thenReturn(gameBoard);
        GameBoard moved = this.gameService.move(gameId, SpotId.SPOT_3);
        Assert.isTrue(moved.getCurrentPlayer().equals(PlayerId.PLAYER_2));
    }
}
