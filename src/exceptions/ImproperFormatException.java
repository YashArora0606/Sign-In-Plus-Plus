package exceptions;

/**
 * Thrown by {@link iomanagement.StudentListReader} when the Excel file it reads from is improperly formatted.
 *
 * @author Alston
 * last updated 12/3/2018
 */
public class ImproperFormatException extends Exception {

    /**
     * Constructs an ImproperFormatException with null as its error message string.
     */
    public ImproperFormatException() {
        super();
    }
}
