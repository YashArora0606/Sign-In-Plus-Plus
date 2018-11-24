package datamanagment;

import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import iomanagement.StudentListReader;
import utilities.Utils;

public class Database {

    private final String[] reasons = new String[] {
            "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
    };

    private final String[] courses = new String[] {
            "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
            "Physical Ed.", "Technology Studies", "Social Sciences", "Lunch / Spare"
    };

    //replace with data structures later
    private Student[] students;

    public Database() {
        new StudentListReader();

    }

    public String[] getReasons() {
        return reasons;
    }

    public String[] getCourses() {
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
