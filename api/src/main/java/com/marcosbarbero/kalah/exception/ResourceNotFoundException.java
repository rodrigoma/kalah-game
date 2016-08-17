package com.marcosbarbero.kalah.exception;

/**
 * @author Marcos Barbero
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String id) {
        super(String.format("The resource with id '%s' was not found.", id));
    }

    public ResourceNotFoundException() {
        super("The resource was not found.");
    }
}
