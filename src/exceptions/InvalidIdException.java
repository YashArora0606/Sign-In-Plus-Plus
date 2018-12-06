package exceptions;

import utilities.Utils;

/**
 * Thrown by {@link datamanagement.SignInManager} when an id that does not exist is passed into it
 *
 * @author Alston
 * last updated 12/3/2018
 */
public class InvalidIdException extends Exception {

    /**
     * Constructs an InvalidIdException
     *
     * @param id the id that caused the InvalidIdException to be thrown
     */
    public InvalidIdException(int id) {
        super(Utils.idToString(id) + " is an invalid student ID");
    }

	public InvalidIdException(String id) {
		super(id + " is an invalid student ID");
	}
}