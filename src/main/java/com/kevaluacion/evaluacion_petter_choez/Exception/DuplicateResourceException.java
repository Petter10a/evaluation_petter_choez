package com.kevaluacion.evaluacion_petter_choez.Exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Custom exception thrown when a resource already exists.
 */
@Getter
@Setter
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail
     * message.
     *
     * @param message the detail message
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
