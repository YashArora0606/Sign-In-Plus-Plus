package datamanagment;
import java.util.Arrays;
import java.util.LinkedList;

import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import iomanagement.ExcelManager;
import iomanagement.StudentListReader;
import utilities.Utils;

/**
 * Database (very underdeveloped)
 */
public class Database {

    private final String[] reasons = new String[] {
            "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
    };

    private final String[] courses = new String[] {
            "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
            "Physical Ed.", "Technology Studies", "Social Sciences", "Lunch / Spare"
    };

    private ExcelManager master;

    //replace with data structures later
    private Student[] students;
    private LinkedList<Session> sessionsToResolve = new LinkedList<>();

    public Database() {

        master = new ExcelManager("MasterList.xlsx");

        students = new StudentListReader("StudentList.xlsx").getStudents();
        Arrays.sort(students);
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

        Session session = findSession(student);
        if (session != null) {
            throw new AlreadyLoggedInException();
        }

        sessionsToResolve.add(new Session(student, courseMissed, reason, course));
        return true;
    }


    public boolean signOut(String id) throws InvalidIdException, NotLoggedInException {
        Student student = findStudent(id);
        if (student == null) { //if no such student exists
            throw new InvalidIdException(id);
        }

        Session session = findSession(student);
        if (session == null) {
            throw new NotLoggedInException();
        }

        session.resolve();

        master.logSession(session);

        return true;
    }

    /**
     * reconcileData()
     * @param unknown
     * @return boolean whether or not the data was successfully able to be reconciled or not
     */
    public boolean reconcileData() {

        return true;
    }

    /**
     * close()
     * Method that closes the document in the ExcelManager ensuring that it is not left open to data leaks
     */
    public void close() {
        master.close();
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

    private Session findSession(Student student) {
        for (Session session : sessionsToResolve) {
            if (session.student.equals(student)) {
                return session;
            }
        }
        return null;
    }
}
