package org.lucassus.jmine.enums;

/**
 * Exception that indicates conditions that user cannot set custom game options.
 */
public class CannotSetCustomGameException extends Exception {

    /**
     * Creates a new instance of <code>CannotSetCustomGameException</code> without detail message.
     */
    public CannotSetCustomGameException() {
    }

    /**
     * Constructs an instance of <code>CannotSetCustomGameException</code> with the specified detail message.
     * @param message the detail message.
     */
    public CannotSetCustomGameException(final String message) {
        super(message);
    }
}
