package datamanagment;

import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;

public class SignInManager {

    private final static String[] reasons = new String[] {
            "Test", "Chill Zone", "Quiet Work", "Academic Help", "Group Work"
    };

    private final static String[] courses = new String[] {
            "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
            "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare"
    };

    private Database database;

    public SignInManager(Database database) {
        this.database = database;
    }

    public static String[] getReasons() {
        return reasons;
    }

    public static String[] getCourses() {
        return courses;
    }

    public void close() {
        database.close();
    }

    public boolean signIn(String id, String course, String reason, String courseMissed) throws InvalidIdException, AlreadyLoggedInException {

        //check if student exists
        Student student = database.findStudent(id);
        if (student == null) {
            throw new InvalidIdException(id);
        }



        return true;
    }

    public boolean signOut(String id) throws InvalidIdException, NotLoggedInException {

        //check if student exists
        Student student = database.findStudent(id);
        if (student == null) { //if no such student exists
            throw new InvalidIdException(id);
        }



        return true;
    }
}
