package com.marcosbarbero.kalah.model.repository;

import com.marcosbarbero.kalah.model.entity.GameBoard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

/**
 * @author Marcos Barbero
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test
    public void testFindById_invalidId_shouldReturnNull() {
        GameBoard gameBoard = this.gameRepository.findById("invalidId");
        Assert.isNull(gameBoard);
    }

    @Test
    public void testFindById_validId_shouldNotReturnNull() {
        GameBoard saved = this.gameRepository.save(new GameBoard());
        GameBoard gameBoard = this.gameRepository.findById(saved.getId());
        Assert.notNull(gameBoard);
        Assert.isTrue(gameBoard.getId().equals(saved.getId()));
    }

    @Test
    public void testSave_shouldNotReturnError() {
        GameBoard saved = this.gameRepository.save(new GameBoard());
        Assert.notNull(saved.getId());
    }
}
