package exceptions;

/**
 * Thrown by SignInManager when {@link datamanagement.SignInManager#signOut(int)}
 * is called on a student who is not signed in
 *
 * @author Alston
 * last updated 12/3/2018
 */
public class NotSignedInException extends Exception {

    /**
     * Constructs a NotSignedInException
     */
    public NotSignedInException() {
        super("That id has not yet logged in!");
    }
}
