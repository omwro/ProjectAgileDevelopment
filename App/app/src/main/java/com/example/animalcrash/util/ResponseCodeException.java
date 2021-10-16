package com.example.animalcrash.util;

/**
 * @author      MEHMET TETIK
 * @version     1.0
 *
 * Purpose      HttpRequest response exception
 *
 */

public class ResponseCodeException extends Exception {

    /**
     * Throw error message
     *
     * @param message String    message
     */
    public ResponseCodeException(String message) {
        super(message);
    }
}
