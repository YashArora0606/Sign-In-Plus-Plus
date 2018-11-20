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

    private final String[] reasons; //different reasons for sign in
    private final String[] subjects; //different subjects for sign in

    private ExcelManager master;

    //replace with data structures later
    private Student[] students;
    private LinkedList<Session> sessionsToResolve = new LinkedList<>();

    public Database() {

        //instead of hardcoding reasons or subjects, we could read from a .txt file to make it more dynamic
        reasons = new String[] {
                "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
        };

        subjects = new String[] {
                "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
                "Physical Ed.", "Technology Studies", "Social Sciences", "Lunch / Spare"
        };

        master = new ExcelManager("MasterList.xlsx");

        students = new StudentListReader("StudentList.xlsx").getStudents();
        Arrays.sort(students);

        for (int i = 0; i < students.length; i++) {
            System.out.print(students[i].id + " ");
        }
    }
    

    public String[] getReasons() {
        return reasons;
    }

    public String[] getSubjects() {
        return subjects;
    }

    
    public boolean signIn(String id, String course, String reason) throws InvalidIdException, AlreadyLoggedInException {
        Student student = findStudent(id);
        if (student == null) { //if no such student exists
            throw new InvalidIdException(id);
        }

        Session session = findSession(student);
        if (session != null) {
            throw new AlreadyLoggedInException();
        }

        sessionsToResolve.add(new Session(student, course, reason));
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
        System.out.println(id + ", " + low + ", " + high);
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
