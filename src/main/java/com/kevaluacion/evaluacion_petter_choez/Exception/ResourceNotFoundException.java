package com.kevaluacion.evaluacion_petter_choez.Exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception thrown when a resource is not found.
 */
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
