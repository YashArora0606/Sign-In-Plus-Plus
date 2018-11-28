package datamanagement;

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
import java.util.List;
import java.util.Properties;


/**
 * Apache Derby Database (SignInSystemDatabase)
 * 11/26/2018
 *
 * @author Alston
 */
public class DerbyDatabase implements Database {

    //con variables
    private Connection con = null;
    private ArrayList<Statement> statements = new ArrayList<>();
    private Statement statement;

    private PreparedStatement insertNewStudent;
    private PreparedStatement findStudent;
    private PreparedStatement checkStudentStatus;
    private PreparedStatement removeStudent;

    private PreparedStatement insertNewSession;
    private PreparedStatement resolveSessions;


    public DerbyDatabase() {
        try {
            //connect to con
            Properties props = new Properties();
            con = DriverManager.getConnection("jdbc:derby:SignInSystemDatabase;create=true", props);

            //statements
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(statement);

            //create tables if they don't exist
            createTablesIfNotExist();


            String addStudentSql = "insert into students values (?, ?, ?, ?)";
            insertNewStudent = con.prepareStatement(addStudentSql);
            statements.add(insertNewStudent);

            String findStudentSql = "select * from students where id=?";
            findStudent = con.prepareStatement(findStudentSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(findStudent);

            String checkStatusSql = "select * from sessions where id=? and signouttime is null";
            checkStudentStatus = con.prepareStatement(checkStatusSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(checkStudentStatus);

            String removeStudentSql = "delete from students where id=?";
            removeStudent = con.prepareStatement(removeStudentSql);
            statements.add(removeStudent);


            String insertSql = "insert into sessions values (?, ?, ?, ?, ?, ?)";
            insertNewSession = con.prepareStatement(insertSql);
            statements.add(insertNewSession);

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

        } catch (SQLException  e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student findStudentById(int id) throws IOException {
        try {
            findStudent.setInt(1, id);

            ResultSet res = findStudent.executeQuery();
            if (!res.isBeforeFirst()) {
                return null;
            }

            res.next();
            Student student = new Student(res.getInt("id"),
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

    public boolean isStudentSignedIn(int id) throws IOException{
        try {
            checkStudentStatus.setInt(1, id);

            ResultSet res =  checkStudentStatus.executeQuery();
            return res.isBeforeFirst();

        } catch (SQLException e) {
            throw new IOException();
        }
    }

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


    public boolean addSession(Session session) {
        try {
            insertNewSession.setInt(1, session.student.id);
            insertNewSession.setTimestamp(2, session.startTime);
            insertNewSession.setTimestamp(3, session.endTime);
            insertNewSession.setString(4, session.reason);
            insertNewSession.setString(5, session.courseWork);
            insertNewSession.setString(6, session.courseMissed);
            insertNewSession.executeUpdate();

            con.commit();

            printSessions(); //debugging code

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Session> findSessions(HashMap<String, Object> criteria) throws IOException, InputMismatchException {

        String query = buildQuery(criteria);

        List<Session> sessionList = new ArrayList<>();
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
                        res.getString("coursework"),
                        res.getString("coursemiss"));
                sessionList.add(session);
            }

            return sessionList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    public boolean resolveOpenSessions(int id, Timestamp time) {
        try {
            //update database
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



    public void close() {
        for (Statement stmt : statements) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void createTablesIfNotExist() {
        try {
            statement.executeUpdate("create table students(" +
                    "id int not null primary key, " +
                    "firstname varchar(50) not null, " +
                    "lastname varchar(50) not null, " +
                    "grade int not null check (grade >= 9 and grade <= 13))");

        } catch (SQLException e) {
            System.out.println("Students table already exists");
        }

        try {
            statement.executeUpdate("create table sessions(" +
                    "id int not null , " +
                    "signintime timestamp not null," +
                    "signouttime timestamp, " +
                    "reason varchar(50) not null, " +
                    "coursework varchar(50) not null, " +
                    "coursemiss varchar(50) not null," +
                    "foreign key(id) references students(id))");

            con.commit();

        } catch (SQLException e) {
            System.out.println("Session table already exists");
        }
    }

    private String buildQuery(HashMap<String, Object> criteria) throws InputMismatchException{
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