package datamanagement;

import utilities.SinglyLinkedList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Properties;


/**
 * Apache Derby Database (SignInSystemDatabase)
 * 11/26/2018
 *
 * @author Alston
 */
public class DerbyDatabase implements Database {

    private Connection con = null;
    private ArrayList<Statement> statements = new ArrayList<>();
    private Statement statement;

    //prepared statements
    private PreparedStatement insertNewStudent;
    private PreparedStatement findStudent;
    private PreparedStatement checkStudentStatus;
    private PreparedStatement removeStudent;

    private PreparedStatement insertNewSession;
    private PreparedStatement resolveSessions;


    /**
     * Constructs and connects to a database
     */
    public DerbyDatabase() {
        try {
            //connect to database
            Properties props = new Properties();
            con = DriverManager.getConnection("jdbc:derby:SignInSystemDatabase;create=true", props);

            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(statement);

            //create tables if they don't exist
            createTablesIfNotExist();

            //create prepared statements

            //inserts a new row into the STUDENTS table
            String addStudentSql = "insert into students values (?, ?, ?, ?)";
            insertNewStudent = con.prepareStatement(addStudentSql);
            statements.add(insertNewStudent);

            //finds a row in the STUDENTS table based on student id
            String findStudentSql = "select * from students where id=?";
            findStudent = con.prepareStatement(findStudentSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(findStudent);

            //finds all unresolved rows in the SESSIONS table by student id
            String checkStatusSql = "select * from sessions where id=? and signouttime is null";
            checkStudentStatus = con.prepareStatement(checkStatusSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(checkStudentStatus);

            //removes a row in the STUDENTS table based on student id
            String removeStudentSql = "delete from students where id=?";
            removeStudent = con.prepareStatement(removeStudentSql);
            statements.add(removeStudent);

            //inserts a new row in the SESSIONS table
            String insertSql = "insert into sessions values (?, ?, ?, ?, ?, ?)";
            insertNewSession = con.prepareStatement(insertSql);
            statements.add(insertNewSession);

            //fills in unresolved signouttime rows in the SESSIONS table by student id
            String signOutSql = "update sessions set signouttime=? where id=? and signouttime is null";
            resolveSessions = con.prepareStatement(signOutSql);
            statements.add(resolveSessions);


            con.commit();

            printStudents();
            printSessions();

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1); //kill the program if the con fails
        }
    }

    /**
     * Adds a student to the database
     *
     * @param student student to be added
     * @return true, if the student was successfully added
     */
    public boolean addStudent(Student student) {
        try {
            insertNewStudent.setInt(1, student.id);
            insertNewStudent.setString(2, student.firstName);
            insertNewStudent.setString(3, student.lastName);
            insertNewStudent.setInt(4, student.grade);
            insertNewStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Queries a student from the database that matches a specified id
     *
     * @param id a specified id
     * @return the student, or null if none was found
     * @throws IOException thrown if any errors occur (e.g. SQLException)
     */
    public Student findStudentById(int id) throws IOException {
        try {
            findStudent.setInt(1, id);

            ResultSet res = findStudent.executeQuery();
            if (!res.isBeforeFirst()) { //if the ResultSet has no row, isBeforeFirst() will return false
                return null;
            }

            res.next();
            Student student = new Student(  //create the student object
                    res.getInt("id"),
                    res.getString("firstname"),
                    res.getString("lastname"),
                    res.getInt("grade"));
            res.close();

            return student;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Verifies if a student with a specified id has previous unresolved sign-in sessions
     *
     * @param id a specified id
     * @return true, if 1+ unresolved sign-in sessions were found
     * @throws IOException thrown if any errors occur (e.g. SQLException)
     */
    public boolean isStudentSignedIn(int id) throws IOException {
        try {
            checkStudentStatus.setInt(1, id);

            ResultSet res = checkStudentStatus.executeQuery();
            return res.isBeforeFirst();

        } catch (SQLException e) {
            throw new IOException();
        }
    }

    /**
     * Removes a student with a specified id from the database
     *
     * @param id a specified database
     * @return true, if the student was successfully removed
     */
    public boolean removeStudentById(int id) {
        try {
            removeStudent.setInt(1, id);
            removeStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Adds a session to the database
     *
     * @param session Session to add
     * @return true, if the session was successfully added
     */
    public boolean addSession(Session session) {
        try {
            insertNewSession.setInt(1, session.student.id);
            insertNewSession.setTimestamp(2, session.startTime);
            insertNewSession.setTimestamp(3, session.endTime);
            insertNewSession.setString(4, session.reason);
            insertNewSession.setString(5, session.sert);
            insertNewSession.setString(6, session.course);
            insertNewSession.executeUpdate();

            con.commit();

            printSessions(); //debugging code

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Queries all the sessions that matches some specified criteria in the following format
     *
     * @param criteria a HashMap with Map.Entry<field, specification>
     * @return a List of the matching Session objects
     * @throws IOException            thrown if any error occurs
     * @throws InputMismatchException thrown if criteria format is incorrect
     */
    public SinglyLinkedList<Session> findSessions(HashMap<String, Object> criteria) throws IOException, InputMismatchException {

        String query = buildQuery(criteria);

        SinglyLinkedList<Session> sessionList = new SinglyLinkedList<>();
        HashMap<Integer, Student> usedStudents = new HashMap<>();

        try {
            ResultSet res = statement.executeQuery(query);

            while (res.next()) {
                int id = res.getInt("id");
                Student student;

                if (usedStudents.containsKey(id)) {
                    student = usedStudents.get(id);
                } else {
                    student = new Student(
                            id,
                            res.getString("firstname"),
                            res.getString("lastname"),
                            res.getInt("grade"));
                    usedStudents.put(id, student);
                }

                Session session = new Session(
                        student,
                        res.getTimestamp("signintime"),
                        res.getTimestamp("signouttime"),
                        res.getString("reason"),
                        res.getString("sert"),
                        res.getString("course"));
                sessionList.add(session);
            }

            return sessionList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Resolves all sessions matching an id by filling the signouttime with the specified time
     *
     * @param id   the id
     * @param time a specified Timestamp time
     * @return true if the open sessions were successfully resolved
     */
    public boolean resolveOpenSessions(int id, Timestamp time) {
        try {
            resolveSessions.setTimestamp(1, time);
            resolveSessions.setInt(2, id);
            resolveSessions.executeUpdate();

            con.commit();

            printSessions(); //debugging code

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Closes the database and all opened resources
     */
    public void close() {
        for (Statement stmt : statements) { //close all statements
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            con.close(); //close connections
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Creates the tables if they do not exist
     */
    private void createTablesIfNotExist() {
        try {
            statement.executeUpdate("create table students(" +
                    "id int not null primary key, " +
                    "firstname varchar(50) not null, " +
                    "lastname varchar(50) not null, " +
                    "grade int not null check (grade >= 9 and grade <= 13))");
            con.commit();

        } catch (SQLException e) { //thrown if the table does not exist
            System.out.println("Students table already exists");
        }

        try {
            statement.executeUpdate("create table sessions(" +
                    "id int not null , " +
                    "signintime timestamp not null," +
                    "signouttime timestamp, " +
                    "reason varchar(50) not null, " +
                    "sert varchar(50) not null, " +
                    "course varchar(50) not null," +
                    "foreign key(id) references students(id))");

            con.commit();

        } catch (SQLException e) { //thrown if the table does not exist
            System.out.println("Session table already exists");
        }
    }


    /**
     * Builds a String SQL from a HashMap of specified criteria
     *
     * @param criteria a HashMap with Map.Entry<field, specification>
     * @return the String SQL query
     * @throws InputMismatchException if the
     */
    private String buildQuery(HashMap<String, Object> criteria) throws InputMismatchException {
        StringBuilder query = new StringBuilder("select * from sessions");

        return query.toString();
    }


//------------------------------------------DEVELOPMENT/DEBUGGING CODE -------------------------------------------------

    private void printStudents() {
        try {
            ResultSet res = statement.executeQuery("select * from students");
            int count = 0;
            while (res.next()) {
                for (int i = 1; i <= 4; i++) {
                    System.out.print(res.getString(i) + ", ");
                }
                count++;
                System.out.println();
            }
            System.out.println(count + " students logged.");
            res.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printSessions() {
        try {
            ResultSet res = statement.executeQuery("select * from sessions");
            int count = 0;
            while (res.next()) {
                for (int i = 1; i <= 9; i++) {
                    System.out.print(res.getString(i) + ", ");
                }
                count++;
                System.out.println();
            }
            System.out.println(count + " sessions logged.");
            res.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}