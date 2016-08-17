package com.marcosbarbero.kalah.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @author Marcos Barbero
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartGameDTO implements Serializable {
    private static final long serialVersionUID = 609344303011163141L;

    @Min(1)
    @Max(6)
    @NotNull
    private Integer stones;
}
