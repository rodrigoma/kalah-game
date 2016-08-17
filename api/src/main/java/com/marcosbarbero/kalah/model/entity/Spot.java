package com.marcosbarbero.kalah.model.entity;

import com.marcosbarbero.kalah.model.entity.enums.SpotId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The spot representation.
 *
 * @author Marcos Barbero
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spot implements Serializable {
    private static final long serialVersionUID = 7039002373660884602L;

    private SpotId id;
    private int stones;
}
