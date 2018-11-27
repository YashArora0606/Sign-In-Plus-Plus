package datamanagment;

import iomanagement.StudentListReader;
import utilities.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Apache Derby Database (SignInSystemDatabase)
 * 11/26/2018
 *
 * @author Alston
 */
public class DerbyDatabase implements Database {

    //list of students
    private final Student[] students;

    //con variables
    private Connection con = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    private Statement statement;
    private PreparedStatement signIn;
    private PreparedStatement signOut;


    public DerbyDatabase() {
        students = new StudentListReader().getStudents();

        try {
            //connect to con
            Properties props = new Properties();
            con = DriverManager.getConnection("jdbc:derby:SignInSystemDatabase", props);

            //statements
            statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statements.add(statement);

            String insertSql = "insert into sessions values (?, ?, ?, ?, ?, null, ?, ?, ?)";
            signIn = con.prepareStatement(insertSql);
            statements.add(signIn);

            String signOutSql = "update sessions set signouttime=? where id=? and signouttime is null";
            signOut = con.prepareStatement(signOutSql);
            statements.add(signOut);

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1); //kill the program if the con fails
        }
    }


    public void close() {

        //close statements
        for (Statement stmt : statements) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //close connections
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Student findStudent(String id) {
        return findStudent(id, 0, students.length - 1);
    }

    private Student findStudent(String id, int low, int high) {
        if (high >= low) {
            int mid = (low + high) / 2;

            if (id.compareTo(students[mid].id) == 0) {
                return students[mid];
            } else if (id.compareTo(students[mid].id) < 0) {
                return findStudent(id, low, mid - 1);
            } else {
                return findStudent(id, mid + 1, high);
            }
        }

        return null;
    }

    public boolean signIn(Session session) {

        try {
            signIn.setString(1, session.student.id);
            signIn.setString(2, session.student.firstName);
            signIn.setString(3, session.student.lastName);
            signIn.setInt(4, Integer.parseInt(session.student.grade));
            signIn.setTimestamp(5, new Timestamp(Utils.getTime()));
            signIn.setString(6, session.reason);
            signIn.setString(7, session.courseWork);
            signIn.setString(8, session.courseMissed);
            signIn.executeUpdate();

            con.commit();

            printSessions(); //debugging code

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean signOut(String id) {
        try {
            //update database
            signOut.setTimestamp(1, new Timestamp(Utils.getTime()));
            signOut.setString(2, id);
            signOut.executeUpdate();

            con.commit();

            printSessions(); //debugging code

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<Session> querySessions(HashMap<String, Object> criterion) throws SQLException {

        String query = "select * from tables";

        List<Session> sessions = new ArrayList<>();

        ResultSet res = statement.executeQuery(query);

        return sessions;
    }


//------------------------------------------DEVELOPMENT/DEBUGGING CODE -------------------------------------------------

    private void printSessions() {
        try {
            ResultSet rs = statement.executeQuery("select * from sessions");
            int count = 0;
            while (rs.next()) {
                for (int i = 1; i <= 9; i++) {
                    System.out.print(rs.getString(i) + ", ");
                    count++;
                }
                System.out.println(count + " results logged.");
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}