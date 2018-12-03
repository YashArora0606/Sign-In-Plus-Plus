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
import java.util.HashMap;
import java.util.Properties;


/**
 * Apache Derby Database (SignInSystemDatabase)
 * 11/26/2018
 *
 * @author Alston
 */
public class DerbyDatabase implements Database {

    private Connection con = null;

    private Statement statement;
    private HashMap<String, PreparedStatement> prepStatements = new HashMap<>();


    public DerbyDatabase() {
        try {
            //connect to database
            Properties props = new Properties();
            con = DriverManager.getConnection("jdbc:derby:SignInSystemDatabase;create=true", props);

            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            //create tables if they don't exist
            createTablesIfNotExist();

            //create prepared statements
            String[][] statements = {
                    {"insert student", "insert into students (id, firstname, lastname, grade) values (?, ?, ?, ?)"},
                    {"find student", "select * from students where id=?"},
                    {"get all students", "select * from students"},
                    {"update student", "update students set firstname=?, lastname=?, grade=? where id=?"},
                    {"check student", "select * from sessions where id=? and signouttime is null"},
                    {"remove student", "delete from students where id=?"},
                    {"insert sessions", "insert into sessions (id, signintime, signouttime, reason, sert, course) values (?, ?, ?, ?, ?, ?)"},
                    {"resolve sessions", "update sessions set signouttime=? where id=? and signouttime is null"},
            };

            for (String[] pair : statements) {
                PreparedStatement prepStmt = con.prepareStatement(pair[1], ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                prepStatements.put(pair[0], prepStmt);
            }

            con.commit();

            printStudents();
            printSessions();

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void createTablesIfNotExist() {
        try {
            statement.executeUpdate("create table students(" +
                    "id int not null primary key, " +
                    "firstname varchar(100) not null, " +
                    "lastname varchar(100) not null, " +
                    "grade int check (grade >= 9 and grade <= 13))");
            con.commit();

        } catch (SQLException e) {
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Session table already exists");
            } else {
                e.printStackTrace();
            }
        }

        try {
            statement.executeUpdate("create table sessions(" +
                    "id int not null , " +
                    "signintime timestamp not null," +
                    "signouttime timestamp, " +
                    "reason varchar(100) not null, " +
                    "sert varchar(100) not null, " +
                    "course varchar(100) not null," +
                    "foreign key(id) references students(id) on delete cascade)");
            con.commit();

        } catch (SQLException e) { //thrown if the table does not exist
            if (e.getSQLState().equals("X0Y32")) {
                System.out.println("Session table already exists");
            } else {
                e.printStackTrace();
            }
        }
    }

    public boolean addStudent(Student newStudent) {
        try {
            PreparedStatement insertStudent = prepStatements.get("insert student");
            insertStudent.setInt(1, newStudent.id);
            insertStudent.setString(2, newStudent.firstName);
            insertStudent.setString(3, newStudent.lastName);
            insertStudent.setInt(4, newStudent.grade);
            insertStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student findStudentById(int id) throws IOException {
        try {
            PreparedStatement findStudent = prepStatements.get("find student");
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

    public SinglyLinkedList<Student> getStudents() {

        SinglyLinkedList<Student> allStudents = new SinglyLinkedList<>();
        PreparedStatement getAllStudents = prepStatements.get("get all students");

        try {
            ResultSet res = getAllStudents.executeQuery();
            while (res.next()) {
                Student student = new Student(
                        res.getInt("id"),
                        res.getString("firstname"),
                        res.getString("lastname"),
                        res.getInt("grade"));
                allStudents.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return allStudents;
    }

    public boolean isStudentSignedIn(int id) throws IOException {
        try {
            PreparedStatement checkStudent = prepStatements.get("check student");
            checkStudent.setInt(1, id);

            ResultSet res = checkStudent.executeQuery();
            return res.isBeforeFirst();

        } catch (SQLException e) {
            throw new IOException();
        }
    }

    public boolean updateStudent(Student updatedStudent) {
        try {
            PreparedStatement updateStudent = prepStatements.get("update student");
            updateStudent.setString(1, updatedStudent.firstName);
            updateStudent.setString(2, updatedStudent.lastName);
            updateStudent.setInt(3, updatedStudent.grade);
            updateStudent.setInt(4, updatedStudent.id);
            updateStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeStudentById(int id) {
        try {
            PreparedStatement removeStudent = prepStatements.get("remove student");
            removeStudent.setInt(1, id);
            removeStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }


    public boolean addSession(Session newSession) {
        try {
            PreparedStatement insertSession = prepStatements.get("insert session");
            insertSession.setInt(1, newSession.student.id);
            insertSession.setTimestamp(2, newSession.startTime);
            insertSession.setTimestamp(3, newSession.endTime);
            insertSession.setString(4, newSession.reason);
            insertSession.setString(5, newSession.sert);
            insertSession.setString(6, newSession.course);
            insertSession.executeUpdate();

            con.commit();

            printSessions(); //debugging code

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public SinglyLinkedList<Session> findSessions(Query query) throws IOException {

        String sqlQuery = buildQuery(query);

        SinglyLinkedList<Session> sessionList = new SinglyLinkedList<>();
        HashMap<Integer, Student> usedStudents = new HashMap<>();

        try {
            ResultSet res = statement.executeQuery(sqlQuery);

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

    private String buildQuery(Query query) {

        StringBuilder sqlQuery = new StringBuilder("select * from sessions where (signouttime is not null)");

        SinglyLinkedList<String> conditions = new SinglyLinkedList<>();

        if (query.id == -1) {
            conditions.add("(id=" + query.id + ")");
        }

        if (query.earliestDate != null) {
            conditions.add("(signintime>" + query.earliestDate + ")");
        }

        if (query.latestDate != null) {
            conditions.add("(signouttime<" + query.latestDate + ")");
        }

        if (query.minTime > 0) {
            conditions.add("({fn timestampdiff(sql_tsi_minute, signintime, signouttime)}>" + query.minTime + ")");
        }

        if (query.maxTime > 0) {
            conditions.add("({fn timestampdiff(sql_tsi_minute, signintime, signouttime)}<" + query.maxTime + ")");
        }

        if (query.reasons.size() > 0) {
            conditions.add("(reason in " + toSqlList(query.reasons) + ")");
        }

        if (query.serts.size() > 0) {
            conditions.add("(sert in " + toSqlList(query.serts) + ")");
        }

        if (query.courses.size() > 0) {
            conditions.add("(course in " + toSqlList(query.courses) + ")");
        }

        if (conditions.size() == 0) {
            return sqlQuery.toString();
        }

        for (String condition : conditions) {
            sqlQuery.append(" and ");
            sqlQuery.append(condition);
        }

        System.out.println(sqlQuery.toString());

        return sqlQuery.toString();
    }

    private String toSqlList(SinglyLinkedList<String> list) {
        if (list.size() == 0) {
            return null;
        }

        StringBuilder sqlList = new StringBuilder("(");

        for (String item : list) {
            sqlList.append("'" + item + "', ");
        }

        sqlList.setCharAt(sqlList.length() - 2, ')');

        return sqlList.toString();
    }

    public boolean resolveOpenSessions(int id, Timestamp time) {
        try {
            PreparedStatement resolveSessions = prepStatements.get("resolve session");
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

        closeStatement(statement);
        for (Statement stmt : prepStatements.values()) { //close all statements
            closeStatement(stmt);
        }

        try {
            con.close(); //close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeStatement(Statement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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