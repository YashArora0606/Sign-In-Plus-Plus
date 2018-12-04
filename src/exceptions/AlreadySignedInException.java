package exceptions;

/**
 * Thrown by SignInManager when {@link datamanagement.SignInManager#signIn(int, String, String, String)}
 * is called on a student who is already signed in
 *
 * @author Alston
 * last updated 12/3/2018
 */
public class AlreadySignedInException extends Exception {

    /**
     * Constructs an AlreadySignedInException
     */
    public AlreadySignedInException() {
        super("That id is already logged in!");
    }
}
