package exceptions;

/**
 * Thrown by SignInManager when {@link datamanagement.SignInManager#removeStudent(int)}
 * is called but no such student with the specified id exists within the database.
 */
public class StudentDoesNotExistException extends Exception {

    /**
     * Constructs a StudentDoesNotExistException with null as its error message string.
     */
    public StudentDoesNotExistException() {
        super();
    }
}
