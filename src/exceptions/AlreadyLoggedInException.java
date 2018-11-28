package exceptions;
public class AlreadyLoggedInException extends Exception {
    public AlreadyLoggedInException() {
        super("That id is already logged in!");
    }
}
