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
    
    /**
     * getReasons()
     * @return String[] reasons of the different possible reasons why someone might enter the room
     */
    public String[] getReasons() {
        return reasons;
    }

    /**
     * getSubjects()
     * @return String[] all the possible subjects that the students may be working on in the room
     */
    public String[] getSubjects() {
        return subjects;
    }

    /**
     * signIn()
     * Method that signs students into the system, starting a session for said student
     * @param String id that is individual to each each student and is checked against in a database of student numbers
     * @param String course that is the subject that each student is working on in the room
     * @param String reason that is why the student is in the room
     * @return boolean whether the student was able to be successfully signed in or not
     */
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
    
    /**
     * signOut()
     * Method that signs students out of the system, logging their sessions
     * @param String id that is individual to each each student and is checked against students that have been signed in already
     * @return boolean whether or not the student was successfully signed out
     */
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
     * close()
     * Method that closes the document in the ExcelManager ensuring that it is not left open to data leaks
     */
    public void close() {
        master.close();
    }
    
    /**
     * reconcileData()
     * @param unknown
     * @return boolean whether or not the data was successfully able to be reconciled or not
     */
    public boolean reconcileData() {
    	return false;
    }

    /**
     *  findStudent()
     *  Method that finds a student object based on their id
     *  @param student id that is individual to each student, calling another findStudent() method to find a student based on id
     *  @return Student that is the student object based on the student number
     */
    private Student findStudent(String id) {
        if (id == null || id.length() != 9 || !Utils.isAnInteger(id)) {
            return null;
        }
        return findStudent(id, 0, students.length - 1);
    }

    /**
     *  findStudent()
     *  Method that finds a student object based on their id and some recursive variables
     *  @param student id that is individual to each student and recursively passed in
     *  @param int low that is the lowest id length
     *  @param int high that is the highest id length
     *  @return Student that is the student object based on the student number
     */
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

    /**
     * findSession()
     * Method that finds a session object based on the student object
     * @param a student object that can be used to find the session
     * @return a session object that is the latest session of the given student
     */
    
    // SHOULDNT THIS RETURN MULTIPLE SESSIONS BECAUSE A STUDENT CAN HAVE MULTIPLE
    // THIS SHOULD BE A SESSION ARRAY RIGHT
    //OR DOES THIS RETURN THE NEWEST SESSION OF A STUDENT
    private Session findSession(Student student) {
        for (Session session : sessionsToResolve) {
            if (session.student.equals(student)) {
                return session;
            }
        }
        return null;
    }
}
