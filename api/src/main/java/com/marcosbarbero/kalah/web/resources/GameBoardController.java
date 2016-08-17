package com.marcosbarbero.kalah.web.resources;

import com.marcosbarbero.kalah.dto.StartGameDTO;
import com.marcosbarbero.kalah.exception.InvalidRequestException;
import com.marcosbarbero.kalah.exception.ResourceNotFoundException;
import com.marcosbarbero.kalah.model.entity.GameBoard;
import com.marcosbarbero.kalah.model.entity.enums.SpotId;
import com.marcosbarbero.kalah.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;

/**
 * RESTful controller to expose the game API.
 *
 * @author Marcos Barbero
 */
@Slf4j
@RestController
@RequestMapping(GameBoardController.URI)
public class GameBoardController {

    protected static final String URI = "/games";

    private final GameService gameService;

    @Autowired
    public GameBoardController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Find a game by id.
     *
     * @param gameId The gameId
     * @return The GameBoard with statusCode OK
     */
    @GetMapping("/{gameId}")
    public ResponseEntity<GameBoard> get(@PathVariable String gameId) {
        Optional<GameBoard> gameBoard = Optional.ofNullable(this.gameService.get(gameId));
        gameBoard.orElseThrow(ResourceNotFoundException::new);
        return ResponseEntity.ok(gameBoard.get());
    }

    /**
     * Create a new game.
     *
     * @param game    A DTO with the number os stones in this game
     * @param result  The BindingResult to validate possible errors
     * @param builder The UriComponentsBuilder to build the location header
     * @return Void response body with statusCode CREATED and Location HTTP Header
     */
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid StartGameDTO game, BindingResult result, UriComponentsBuilder builder) {
        if (result.hasErrors()) {
            throw new InvalidRequestException(result);
        }
        GameBoard board = this.gameService.create(game.getStones());
        return ResponseEntity.created(builder.path(URI + "/{id}").buildAndExpand(board.getId()).toUri()).build();
    }

    /**
     * Make a move.
     *
     * @param gameId The game id
     * @param spotId The spotId representation to make a move
     * @return The GameBoard with statusCode OK
     */
    @PostMapping("/{gameId}/move/{spotId}")
    public ResponseEntity<GameBoard> move(@PathVariable String gameId, @PathVariable SpotId spotId) {
        return ResponseEntity.ok(this.gameService.move(gameId, spotId));
    }

}
