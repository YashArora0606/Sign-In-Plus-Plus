package exceptions;

import utilities.Utils;

/**
 * Thrown when the id is invalidly formatted
 */
public class InvalidIdException extends Exception {

    public InvalidIdException(int id) {
        super("\"" + Utils.idToString(id) + "\"" + " is an invalid student ID");
    }
}
