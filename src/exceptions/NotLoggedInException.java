package exceptions;
public class NotLoggedInException extends Exception {
    public NotLoggedInException() {
        super("That id has not yet logged in!");
    }
}
