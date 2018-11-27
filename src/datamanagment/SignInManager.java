package datamanagment;

import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import iomanagement.StudentListReader;
import utilities.Utils;

public class SignInManager {

    private final String STUDENTS_DIR = "database/students";
    private final String MASTERLOG_FILE = "database/MasterLog.xlsx";

    private final static String[] reasons = new String[] {
            "Test", "Chill Zone", "Quiet Work", "Academic Help", "Group Work"
    };

    private final static String[] courses = new String[] {
            "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
            "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare"
    };

    //replace with data structures later
    private Student[] students;

    public SignInManager() {
        students = new StudentListReader().getStudents();
    }

    public static String[] getReasons() {
        return reasons;
    }

    public static String[] getCourses() {
        return courses;
    }

    public boolean signIn(String id, String course, String reason, String courseMissed) throws InvalidIdException, AlreadyLoggedInException {
        Student student = findStudent(id);
        if (student == null) { //if no such student exists
            throw new InvalidIdException(id);
        }

        return true;
    }

    public boolean signOut(String id) throws InvalidIdException, NotLoggedInException {
        Student student = findStudent(id);
        if (student == null) { //if no such student exists
            throw new InvalidIdException(id);
        }

        return true;
    }

    public boolean reconcileData() {
        return true;
    }

    public void close() {

    }

    private Student findStudent(String id) {
        if (id == null || id.length() != 9 || !Utils.isAnInteger(id)) {
            return null;
        }
        return findStudent(id, 0, students.length - 1);
    }

    private Student findStudent(String id, int low, int high) {
        if (high >= low) {
            int mid = (low + high)/2;

            if (id.compareTo(students[mid].id) == 0) {
                return students[mid];
            } else if (id.compareTo(students[mid].id) < 0) {
                return findStudent(id, low, mid-1);
            } else {
                return findStudent(id, mid+1, high);
            }
        }

        return null;
    }
}
