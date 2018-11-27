package datamanagment;

import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import iomanagement.StudentListReader;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.Utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Apache Derby Database (SignInSystemDatabase)
 * 11/26/2018
 *
 * @author Alston
 */
public class Database {

    private final Student[] students;

    private final String[] reasons = new String[]{
            "Test", "Chill Zone", "Quiet Work", "Academic Support", "Group Work"
    };

    private final String[] courses = new String[]{
            "Art", "Math", "Music", "Science", "History", "Geography", "Business", "Family Studies",
            "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare"
    };

    //con variables
    private Connection con = null;
    private ArrayList<Statement> statements = new ArrayList<Statement>(); // list of Statements, PreparedStatements
    private Statement statement;
    private PreparedStatement signIn;
    private PreparedStatement signOut;


    /**
     * Constructs and connects to SignInSystemDatabase
     */
    public Database() {
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


    /**
     * @return a String array of possible sign-in reasons
     */
    public String[] getReasons() {
        return reasons;
    }

    /**
     * @return a String array of the school courses or subjects
     */
    public String[] getCourses() {
        return courses;
    }

    /**
     * Signs in a student and logs it
     *
     * @param id           the student's id
     * @param courseWork   the course/subject they are working on in the room
     * @param reason       the reason they are signing in for
     * @param courseMissed the course/subject they are missing
     * @return true, if the student was successfully signed in
     * @throws InvalidIdException       thrown if the id is improperly formatted or doesn't exist
     * @throws AlreadyLoggedInException thrown if the student is already logged in
     */
    public boolean signIn(String id, String courseWork, String reason, String courseMissed) throws InvalidIdException, AlreadyLoggedInException {

        // search for validity of the id
        Student student = findStudent(id);
        if (!Utils.isValidId(id) || student == null) {
            throw new InvalidIdException(id);
        }

        try {

            //search for previous sign-ins
            String signInQuery = "select * from sessions where id='" + student.id + "' and signouttime is null";
            ResultSet prevSignIns = statement.executeQuery(signInQuery);
            if (sizeOfResults(prevSignIns) > 0) {
                throw new AlreadyLoggedInException();
            }
            prevSignIns.close();

            //log student
            signIn.setString(1, student.id);
            signIn.setString(2, student.firstName);
            signIn.setString(3, student.lastName);
            signIn.setInt(4, Integer.parseInt(student.grade));
            signIn.setTimestamp(5, new Timestamp(Utils.getTime()));
            signIn.setString(6, reason);
            signIn.setString(7, courseWork);
            signIn.setString(8, courseMissed);
            signIn.executeUpdate();

            con.commit();

            printSessions(); //debugging code

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Signs out a student
     *
     * @param id the student's id
     * @return true, if the student was successfully signed out
     * @throws InvalidIdException   thrown if the id is improperly formatted or doesn't exist
     * @throws NotLoggedInException thrown if the student is not logged in
     */
    public boolean signOut(String id) throws InvalidIdException, NotLoggedInException {

        Student student = findStudent(id);
        if (!Utils.isValidId(id) || student == null) { //if no such student exists
            throw new InvalidIdException(id);
        }

        try {

            //search for previous sign-ins
            String signInQuery = "select * from sessions where id='" + student.id + "' and signouttime is null";
            ResultSet prevSignIns = statement.executeQuery(signInQuery);
            if (sizeOfResults(prevSignIns) == 0) {
                throw new NotLoggedInException();
            }
            prevSignIns.close();

            //update database
            signOut.setTimestamp(1, new Timestamp(Utils.getTime()));
            signOut.setString(2, student.id);
            signOut.executeUpdate();

            con.commit();

            printSessions(); //debugging code

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Generates an .xlsx file holding all the sessions specified in the sql query
     * @param title title of .xlsx file
     * @param sql query
     */
    public void generateLog(String title, String sql) {

        //query all the sessions and save them
        List<Session> sessions;

        try {
            sessions = extractSessions(sql);

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        if (title.equals("")) {
            title = "Database";
        }

        //create workbook
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet log = workbook.createSheet(title);

        //create styles
        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat((short)22);

        //create header
        String[] header = {"ID", "First Name", "Last Name", "Grade", "Sign-In", "Sign-Out", "Reason", "Subject Work", "Subject Missed"};
        XSSFRow headerRow = log.createRow(0);
        for (int col = 0; col < header.length; col++) {
            headerRow.createCell(col).setCellValue(header[col]);
        }

        //write sessions
        for (int rowIndex = 1; rowIndex <= sessions.size(); rowIndex++) {
            XSSFRow row = log.createRow(rowIndex);
            Session session = sessions.remove(0);

            for (int i = 0; i < 9; i++) {
                row.createCell(i);
            }

            row.getCell(0).setCellValue(session.student.id);
            row.getCell(1).setCellValue(session.student.firstName);
            row.getCell(2).setCellValue(session.student.lastName);
            row.getCell(3).setCellValue(session.student.grade);
            row.getCell(6).setCellValue(session.reason);
            row.getCell(7).setCellValue(session.subjectWork);
            row.getCell(8).setCellValue(session.courseMissed);

            row.getCell(4).setCellStyle(dateStyle);
            row.getCell(4).setCellValue(session.startTime);

            row.getCell(5).setCellStyle(dateStyle);
            row.getCell(5).setCellValue(session.endTime);
        }

        //write to file and open it
        try {
            File file = new File("database/" + title + ".xlsx");
            int i = 1;
            while (file.exists()) {
                file = new File("database/" + title + "(" + i + ").xlsx");
                i++;
            }
            file.createNewFile();

            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File("database/"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the con and all resources
     */
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

    private Student findStudent(String id) {
        if (id == null || id.length() != 9 || !Utils.isAnInteger(id)) {
            return null;
        }
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


    /**
     * @param res
     * @return the number of entries in a result set
     * @throws SQLException
     */
    private int sizeOfResults(ResultSet res) throws SQLException {
        int size = 0;
        while (res.next()) {
            size++;
        }
        res.beforeFirst();
        return size;
    }

    private List<Session> extractSessions(String sql) throws SQLException{
        List<Session> sessions = new ArrayList<>();

        ResultSet res = statement.executeQuery(sql);
        while (res.next()) {

        }
        res.close();

        return sessions;
    }


//------------------------------------------DEVELOPMENT/DEBUGGING CODE -------------------------------------------------


    private void printSessions() {
        try {
            ResultSet rs = statement.executeQuery("select * from sessions");
            System.out.println(sizeOfResults(rs) + " sessions logged");
            while (rs.next()) {
                for (int i = 1; i <= 9; i++) {
                    System.out.print(rs.getString(i) + ", ");
                }
                System.out.println();
            }
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
