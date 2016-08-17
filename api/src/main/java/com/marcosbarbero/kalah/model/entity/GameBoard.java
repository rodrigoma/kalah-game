package com.marcosbarbero.kalah.model.entity;

import com.marcosbarbero.kalah.model.entity.enums.PlayerId;
import lombok.Data;

import java.io.Serializable;

/**
 * The game board representation.
 *
 * @author Marcos Barbero
 */
@Data
public class GameBoard implements Serializable {
    private static final long serialVersionUID = -6839061417681574308L;

    private String id;
    private Player firstPlayer;
    private Player secondPlayer;
    private PlayerId currentPlayer;
}
