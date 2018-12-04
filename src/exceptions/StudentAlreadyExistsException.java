package exceptions;

/**
 * Thrown by SignInManager when {@link datamanagement.SignInManager#addStudent(int, String, String, int)}
 * is called, but a student with the same id already exists within the database.
 *
 * @author Alston
 * last updated 12/03/2018
 */
public class StudentAlreadyExistsException extends Exception {

    /**
     * Constructs an StudentAlreadyExistsException with null as its error message string.
     */
    public StudentAlreadyExistsException() {
        super();
    }
}
