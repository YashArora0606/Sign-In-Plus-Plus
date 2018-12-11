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
 * Implementation of {@link Database} with Apache Derby
 *
 * @author Alston
 * last updated on 12/9/2018
 */
public class DerbyDatabase implements Database {

    private Connection con = null;

    private Statement statement;

    //a HashMap of prepared statements in the format (statement name, statement)
    private HashMap<String, PreparedStatement> prepStatements = new HashMap<>();

    /**
     * Constructs a DerbyDatabase by connecting to the SQL database
     */
    public DerbyDatabase() {
        try {

            //connect to database
            Properties props = new Properties();
            con = DriverManager.getConnection("jdbc:derby:SignInSystemDatabase;create=true", props);

            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            //create tables if they don't exist
            createTablesIfNotExist();

            //a 2D array of statements where String[i][0] is the statement name and String[i][1] is the sql query
            String[][] statements = {

                    //insert a student into the STUDENTS table given their id, first name, last name, and grade
                    {"insert student", "insert into students (id, firstname, lastname, grade) values (?, ?, ?, ?)"},

                    //get the student with a specified id from the STUDENTS table
                    {"find student", "select * from students where id=?"},

                    //get all students from the STUDENTS table
                    {"get all students", "select * from students"},

                    //sets the first name, last name, and grade of a student with a specified id
                    {"update student", "update students set firstname=?, lastname=?, grade=? where id=?"},

                    //get the session(s) with a specified id and unresolved sign-out time from the SESSIONS table
                    {"check student", "select * from sessions where id=? and signouttime is null"},

                    //remove the student with a specified id from the STUDENTS table
                    {"remove student", "delete from students where id=?"},

                    //insert a session into the SESSIONS table given its id, sign-in time, sign-out time, reason, SERT, and course
                    {"insert session", "insert into sessions (id, signintime, signouttime, reason, sert, course) values (?, ?, ?, ?, ?, ?)"},

                    //set the sign-out time of the sessions with a specified id and unresolved sign-out time
                    {"resolve sessions", "update sessions set signouttime=? where id=? and signouttime is null"},

                    //insert a SERT into the SERTS table given their name
                    {"insert sert", "insert into serts(sert) values (?)"},

                    //get all SERTs from the SERTS table
                    {"get all serts", "select * from serts"},

                    //remove all SERTs from the SERTS table
                    {"remove all serts", "delete from serts"}
            };

            //create prepared statements and insert them into the HashMap
            for (String[] pair : statements) {
                PreparedStatement prepStmt = con.prepareStatement(pair[1], ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                prepStatements.put(pair[0], prepStmt);
            }

            con.commit();

            printStudents();
            printSessions();

        } catch (SQLException e) { //kill the whole program if the database cannot be connected to
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates the SQL tables if they do not exist
     */
    private void createTablesIfNotExist() {

        //an array of the SQL commands used to create the tables
        String[] createTableSqls = {

                //the table that holds all the students
                "create table students(" +
                        "id int not null primary key, " +
                        "firstname varchar(100) not null, " +
                        "lastname varchar(100) not null, " +
                        "grade int check (grade >= 9 and grade <= 13))",

                //the table that holds all the student sessions
                //it is bound to the student table in a primary-foreign key relationship
                "create table sessions(" +
                        "id int not null references students(id) on delete cascade, " +
                        "signintime timestamp not null," +
                        "signouttime timestamp, " +
                        "reason varchar(100) not null, " +
                        "sert varchar(100) not null, " +
                        "course varchar(100) not null)",

                //the table that holds all the SERTs
                "create table serts(" +
                        "sert varchar(100) not null unique)"
        };

        for (String sqlCommand : createTableSqls) { //iterate through and create the tables
            try {
                statement.executeUpdate(sqlCommand);
                con.commit();

            } catch (SQLException e) { //a specific SQLException is thrown if the table already exists
                if (!e.getSQLState().equals("X0Y32")) {  //it has code X0Y32, and we ignore it
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Adds a new student to the STUDENTS table
     *
     * @param newStudent a new specified student
     * @return true, if the student was successfully added; false otherwise
     */
    @Override
    public boolean addStudent(Student newStudent) {
        try {
            PreparedStatement insertStudent = prepStatements.get("insert student");

            //fill in the parameters (?) and execute the SQL statement
            insertStudent.setInt(1, newStudent.id);
            insertStudent.setString(2, newStudent.firstName);
            insertStudent.setString(3, newStudent.lastName);
            insertStudent.setInt(4, newStudent.grade);
            insertStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) { //some error has occurred, meaning the student was not added
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Queries a student with a specified student id from the STUDENTS table
     *
     * @param id a student id
     * @return the queried student, or null if no student with the specified id is found
     * @throws IOException thrown if a SQLException is thrown
     */
    @Override
    public Student findStudentById(int id) throws IOException {
        try {
            PreparedStatement findStudent = prepStatements.get("find student");
            findStudent.setInt(1, id);

            ResultSet res = findStudent.executeQuery();
            if (!res.isBeforeFirst()) { //if the ResultSet has no rows, isBeforeFirst() will return false
                res.close();
                return null;
            }

            res.next(); //move to the first row of the ResultSet
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
     * @return a list of the students currently entered in the STUDENTS table
     */
    @Override
    public SinglyLinkedList<Student> getStudents() {

        SinglyLinkedList<Student> allStudents = new SinglyLinkedList<>(); //list of the students
        PreparedStatement getAllStudents = prepStatements.get("get all students");

        try {
            ResultSet res = getAllStudents.executeQuery();
            while (res.next()) { //iterate through the rows
                Student student = new Student( //build the students
                        res.getInt("id"),
                        res.getString("firstname"),
                        res.getString("lastname"),
                        res.getInt("grade"));
                allStudents.add(student);
            }
            res.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null; //null is returned if a SQLException occurs
        }

        return allStudents;
    }

    /**
     * Verifies if a student with a specified student id is currently signed in.
     * A student is currently signed if he or she has one or more unresolved sessions.
     * If no such student is found in the database, false is returned
     *
     * @param id a student id
     * @return true, if the student is found and is currently signed in; false otherwise
     * @throws IOException thrown if a SQLException is thrown
     */
    @Override
    public boolean isStudentSignedIn(int id) throws IOException {
        try {
            PreparedStatement checkStudent = prepStatements.get("check student");
            checkStudent.setInt(1, id);

            ResultSet res = checkStudent.executeQuery();
            boolean signedIn = res.isBeforeFirst(); //isBeforeFirst() returns false if the ResultSet is empty
            res.close();

            return signedIn;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Replaces a student with another student that shares the same student id.
     * If no such student is found in the database, no update is made.
     *
     * @param updatedStudent the new updated student
     * @return true, if the student was found and updated; false otherwise
     */
    @Override
    public boolean updateStudent(Student updatedStudent) {
        try {
            PreparedStatement updateStudent = prepStatements.get("update student");

            //fill in the parameters (?) and execute the SQL statement
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
            return false; //a SQLException occurs, the student is not updated and false is returned
        }
    }

    /**
     * Removes a student with a specified student id from the STUDENTS table
     * If no such student is found in the Database, no removal is made.
     *
     * @param id a student id
     * @return true, if the student was found and removed; false otherwise
     */
    @Override
    public boolean removeStudentById(int id) {
        try {
            PreparedStatement removeStudent = prepStatements.get("remove student");
            removeStudent.setInt(1, id);
            removeStudent.executeUpdate();

            con.commit();

            printStudents();

            return true;

        } catch (SQLException e) {
            return false; //a SQLException occurs, the student is not removed and false is returned
        }
    }

    /**
     * Adds a new resolved or unresolved session to the SESSIONS table
     *
     * @param newSession the new session to be added
     * @return true, if the session was successfully added; false otherwise
     */
    @Override
    public boolean addSession(Session newSession) {
        try {
            PreparedStatement insertSession = prepStatements.get("insert session");

            //fill in the parameters (?) and execute the SQL statement
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
            return false; //a SQLException occurs, the session is not added and false is returned
        }
    }

    /**
     * Queries sessions from the SESSIONS table that match some specified criteria.
     *
     * @param query a Query object containing the criteria
     * @return a list of the retrieved Sessions, or an empty list of no Sessions are retrieved
     * @throws IOException thrown if an SQLException is thrown
     */
    @Override
    public SinglyLinkedList<Session> findSessions(Query query) throws IOException {

        String sqlQuery = buildQuery(query);
        System.out.println(sqlQuery);

        //a list of queried sessions
        SinglyLinkedList<Session> sessionList = new SinglyLinkedList<>();

        //a HashMap in the form (id, student) that memorizes any queried students
        //used to avoid unnecessarily calling findStudentById(id) multiple times on the same id
        HashMap<Integer, Student> usedStudents = new HashMap<>();

        try {
            ResultSet res = statement.executeQuery(sqlQuery);

            while (res.next()) { //iterate through all the rows

                int id = res.getInt("id");

                Student student;
                if (usedStudents.containsKey(id)) { //if the student was already memorized
                    student = usedStudents.get(id); //retrieve it from the HashMap

                } else {
                    student = findStudentById(id); //query the student from the database
                    usedStudents.put(id, student); //memorize it
                }

                //build the session object and add it to the list
                Session session = new Session(
                        student,
                        res.getTimestamp("signintime"),
                        res.getTimestamp("signouttime"),
                        res.getString("reason"),
                        res.getString("sert"),
                        res.getString("course"));
                sessionList.add(session);
            }

            res.close();

            return sessionList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    /**
     * Converts a Query object into a SQL query command
     *
     * @param query the query to be converted
     * @return the SQL query command
     */
    private String buildQuery(Query query) {

        //by default, the query will pull all completed sessions
        StringBuilder sqlQuery = new StringBuilder("select * from sessions where (signouttime is not null)");

        //the conditions specified by the query object in SQL format
        SinglyLinkedList<String> conditions = new SinglyLinkedList<>();


        //below are the if-statements that convert each field of Query into an SQL condition
        //the ordering of the if-statements is parallel to their respective position in Query

        if (query.id >= 0) {
            conditions.add("(id=" + query.id + ")");
        }

        if (query.firstName != null) {
            conditions.add("(id in (select id from students where firstname='" + query.firstName + "'))");
        }

        if (query.lastName != null) {
            conditions.add("(id in (select id from students where lastname='" + query.lastName + "'))");
        }

        if (query.grade >= 0) {
            conditions.add("(id in (select id from students where grade=" + query.grade + "))");
        }

        if (query.earliestTime != null) {
            conditions.add("(signintime>" + query.earliestTime + ")");
        }

        if (query.latestTime != null) {
            conditions.add("(signouttime<" + query.latestTime + ")");
        }

        if (query.minTime > 0) {
            conditions.add("({fn timestampdiff(sql_tsi_minute, signintime, signouttime)}>=" + query.minTime + ")");
        }

        if (query.maxTime > 0) {
            conditions.add("({fn timestampdiff(sql_tsi_minute, signintime, signouttime)}<=" + query.maxTime + ")");
        }

        if (query.reasons != null && query.reasons.size() > 0) {
            conditions.add("(reason in " + toSqlList(query.reasons) + ")");
        }

        if (query.serts != null && query.serts.size() > 0) {
            conditions.add("(sert in " + toSqlList(query.serts) + ")");
        }

        if (query.courses != null && query.courses.size() > 0) {
            conditions.add("(course in " + toSqlList(query.courses) + ")");
        }

        //if no conditions were specified, return the default SQL query
        if (conditions.size() == 0) {
            return sqlQuery.toString();
        }

        //concatenate the conditions with "and"
        for (String condition : conditions) {
            sqlQuery.append(" and ");
            sqlQuery.append(condition);
        }

        return sqlQuery.toString();
    }

    /**
     * Converts a list of Strings into its SQL equivalent.
     * If the list was {"a", "b", "c", "d",...}, the SQL equivalent would be
     * "('a', 'b', 'c', 'd',...)"
     *
     * @param list the list of Strings to be converted
     * @return the SQL representation of the list argument
     */
    private String toSqlList(SinglyLinkedList<String> list) {

        if (list.size() == 0) { //empty lists will not be coverted
            return null;
        }

        StringBuilder sqlList = new StringBuilder("(");

        for (String item : list) { //add "item, " to the SQL list
            sqlList.append("'" + item + "', ");
        }

        //replace the last comma with a ')'
        sqlList.setCharAt(sqlList.length() - 2, ')');

        return sqlList.toString();
    }

    /**
     * Resolves all unresolved sessions referencing a Student with the specified id.
     * A session is resolved by setting its {@link Session#endTime} from null to the time argument
     *
     * @param id   a student id
     * @param time the end time of the unresolved sessions
     * @return true, if any amount (0+) of sessions were successfully found and resolved; false otherwise
     */
    public boolean resolveOpenSessions(int id, Timestamp time) {
        try {
            PreparedStatement resolveSessions = prepStatements.get("resolve sessions");

            //fill in the parameters (?) and execute the SQL statement
            resolveSessions.setTimestamp(1, time);
            resolveSessions.setInt(2, id);
            resolveSessions.executeUpdate();

            con.commit();

            printSessions(); //debugging code

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; //a SQLException occurs and false is returned
        }
    }

    /**
     * Returns a list of the names of the SERTs stored within the SERTS table
     *
     * @return a list of SERT names, or an empty list if a SQLException occurs
     */
    public SinglyLinkedList<String> getSerts() {
        try {
            PreparedStatement getAllSerts = prepStatements.get("get all serts");
            ResultSet res = getAllSerts.executeQuery();

            //list of SERTs stored within the database
            SinglyLinkedList<String> serts = new SinglyLinkedList<>();
            while (res.next()) {
                serts.add(res.getString("sert"));
            }

            res.close();

            return serts;

        } catch (SQLException e) {
            e.printStackTrace();
            return new SinglyLinkedList<>();
        }
    }

    /**
     * Replaces the SERTs logged in the database with a new list of SERTs
     *
     * @param newSerts the new list of SERTs that will replace the old one
     * @return true, if the table was successfully updated; false otherwise
     */
    public boolean replaceSerts(SinglyLinkedList<String> newSerts) {
        try {
            PreparedStatement removeAllSerts = prepStatements.get("remove all serts");
            removeAllSerts.executeUpdate(); //remove all SERTs from the SERTS table

            //add all the new SERTs one by one
            for (String sert : newSerts) {
                PreparedStatement addSert = prepStatements.get("insert sert");
                addSert.setString(1, sert);
                addSert.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false; //a SQLException occurs, all the SERTs were not properly added and false is returned
        }
    }

    /**
     * Closes the database by closing the connection, statements and result set
     */
    public void close() {

        //close all statements and prepared statements
        closeStatement(statement);
        for (Statement stmt : prepStatements.values()) {
            closeStatement(stmt);
        }

        try {
            if (con != null) {
                con.close(); //close connection
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes a statement
     *
     * @param stmt the statement to be closed
     */
    private void closeStatement(Statement stmt) {
        if (stmt == null) {
            return;
        }

        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


//------------------------------------------DEVELOPMENT/DEBUGGING CODE -------------------------------------------------
// please ignore this code as it has no impact on functionality and is used to print the students and sessions

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
            res.close();
            System.out.println(count + " students logged.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printSessions() {
        try {
            ResultSet res = statement.executeQuery("select * from sessions");
            int count = 0;
            while (res.next()) {
                for (int i = 1; i <= 6; i++) {
                    System.out.print(res.getString(i) + ", ");
                }
                count++;
                System.out.println();
            }
            res.close();
            System.out.println(count + " sessions logged.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
