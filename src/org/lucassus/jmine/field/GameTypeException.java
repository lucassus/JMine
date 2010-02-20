package org.lucassus.jmine.field;

public class GameTypeException extends Exception {

    /**
     * Creates a new instance of <code>GameTypeException</code> without detail message.
     */
    public GameTypeException() {
    }

    /**
     * Constructs an instance of <code>GameTypeException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public GameTypeException(String msg) {
        super(msg);
    }
}
