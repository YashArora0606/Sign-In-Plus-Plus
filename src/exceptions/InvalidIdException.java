package exceptions;

/**
 * Thrown when the id is invalidly formatted
 */
public class InvalidIdException extends Exception {
    public InvalidIdException(String id) {
        super("\"" + id + "\"" + " is an invalid student ID");
    }
}
