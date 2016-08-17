package com.marcosbarbero.kalah.model.entity;

import com.marcosbarbero.kalah.model.entity.enums.PlayerId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * The player representation.
 *
 * @author Marcos Barbero
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {
    private static final long serialVersionUID = 4482359968134304743L;

    private PlayerId id;
    private List<Spot> spots = new LinkedList<>();
    private House house;
}
