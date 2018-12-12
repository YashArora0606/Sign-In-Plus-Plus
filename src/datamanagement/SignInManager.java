package datamanagement;

import exceptions.AlreadySignedInException;
import exceptions.ImproperFormatException;
import exceptions.InvalidIdException;
import exceptions.NotSignedInException;
import exceptions.StudentAlreadyExistsException;
import exceptions.StudentDoesNotExistException;
import iomanagement.HTMLWriter;
import iomanagement.StudentListReader;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.SinglyLinkedList;
import utilities.Utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Manager that mediates information between the UI and database
 *
 * @author Alston
 * last updated 12/11/2018
 */
public class SignInManager {

    private Database database;

    /**
     * Constructs a SignInManager that manages the database argument
     *
     * @param database the database the manager edits and queries
     */
    public SignInManager(Database database) {
        this.database = database;
    }

    /**
     * Adds a new Student to the database, if they do not already exist
     *
     * @param id        the student id of the student to be added
     * @param firstName the first name of the student to be added
     * @param lastName  the last name of the student to be added
     * @param grade     the grade of the student to be added
     * @return true, if the student was successfully added; false otherwise
     * @throws StudentAlreadyExistsException thrown if a student with the id argument already exists within the database
     */
    public boolean addStudent(int id, String firstName, String lastName, int grade)
            throws StudentAlreadyExistsException {

        Student existingStudent;

        try {
            existingStudent = database.findStudentById(id); //try to find an existing student
        } catch (IOException e) {
            e.printStackTrace();
            return false; //it is unknown whether there is an existing student so false is returned
        }

        if (existingStudent != null) {
            throw new StudentAlreadyExistsException(); //throw the error if an existing student was found
        }

        Student newStudent = new Student(id, firstName, lastName, grade); //create the student
        return database.addStudent(newStudent); //pass it to the database
    }

    /**
     * Configures the database from an .csv file of students.
     * Any existing students in the database are replaced by those detailed in the .csv file,
     * and their sessions are deleted with them. If a student id persists in both the database and
     * .csv file, the student is updated instead and their sessions are not deleted.
     *
     * @throws IOException             thrown if the .csv file fails to be accessed
     * @throws ImproperFormatException thrown if the .csv file is improperly formatted
     */
    public void configureStudents() throws IOException, ImproperFormatException {

        //students detailed by the .csv file
        SinglyLinkedList<Student> newStudents = StudentListReader.getStudentList();

        //students existing in the database
        SinglyLinkedList<Student> currStudents = database.getStudents();

        for (Student newStudent : newStudents) {
            if (currStudents.indexOf(newStudent) == -1) { //if a student only exists in the .csv file, add it
                database.addStudent(newStudent);
            } else {  //otherwise, student is both in the database and .csv so they are updated instead
                database.updateStudent(newStudent);
            }
        }

        //remove all students in the current database that are not included in the .csv file
        for (Student currStudent : currStudents) {
            if (newStudents.indexOf(currStudent) == -1) {
                database.removeStudentById(currStudent.id);
            }
        }
    }

    /**
     * Removes a student with a specified id from the database, if they exist
     *
     * @param id the id of the student to be removed
     * @return true if the student is successfully removed; false otherwise
     * @throws StudentDoesNotExistException thrown if no student with the id argument exists within the database
     */
    public boolean removeStudent(int id) throws StudentDoesNotExistException {

        Student existingStudent;

        try {
            existingStudent = database.findStudentById(id); //try to find an existing student
        } catch (IOException e) {
            e.printStackTrace();
            return false; //it is unknown whether there is an existing student so false is returned
        }

        if (existingStudent == null) { //throw an error if no existing student was found
            throw new StudentDoesNotExistException();
        }

        return database.removeStudentById(id); //remove the student
    }

    /**
     * Signs in a student by adding an unresolved session to the database
     * if no unresolved sessions for the student with the specified id exist
     *
     * @param id     the student id of the student whom the session belongs to
     * @param reason the reason for the session
     * @param sert   the SERT of the student whom the session belongs to
     * @param course the course the student is missing
     * @return true, if the session was successfully added; false otherwise
     * @throws InvalidIdException       thrown if no student with the id argument exists
     * @throws AlreadySignedInException thrown if the student with the id argument already has an unresolved session(s)
     */
    public boolean signIn(int id, String reason, String sert, String course)
            throws InvalidIdException, AlreadySignedInException {

        Student student;

        try {
            student = database.findStudentById(id);  //check if student exists
        } catch (IOException e) {
            e.printStackTrace();
            return false; //it is unknown whether there is an existing student so false is returned
        }

        if (student == null) { //the student does not exist, the id is invalid and an error is thrown
            throw new InvalidIdException(id);
        }

        try { //check to see if the student is already signed in
            if (database.isStudentSignedIn(id)) {
                throw new AlreadySignedInException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; //it is unknown if the student is already signed in so false is returned
        }

        //create the new session and sign in
        Session session = new Session(student, Utils.getNow(), null, reason, sert, course);
        return database.addSession(session);
    }

    /**
     * Signs out a student by resolving all sessions in the database with the current time
     * if such sessions exist within the database
     *
     * @param id the id of the student who is signing out
     * @return true, if the sessions were successfully resolved; false otherwise
     * @throws InvalidIdException   thrown if no student with the id argument exists
     * @throws NotSignedInException thrown if the student with the id argument has no unresolved sessions
     */
    public boolean signOut(int id) throws InvalidIdException, NotSignedInException {

        Student student;

        try {
            student = database.findStudentById(id); //check if student exists
        } catch (IOException e) {
            e.printStackTrace();
            return false; //it is unknown whether there is an existing student so false is returned
        }

        if (student == null) { //the student does not exist, the id is invalid and an error is thrown
            throw new InvalidIdException(id);
        }

        try { //check to see if the student is already signed in
            if (!database.isStudentSignedIn(id)) {
                throw new NotSignedInException();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; //it is unknown if the student is already signed in so false is returned
        }

        //resolve all sessions
        return database.resolveOpenSessions(id, Utils.getNow());
    }


    /**
     * @return an array of suggested reasons the student is signing in for
     */
    public String[] getReasons() {
        return new String[]{"Test", "Chill Zone", "Quiet Work", "Academic Help", "Group Work"};
    }

    /**
     * @return an array of courses or subjects the student is working on in the room
     */
    public String[] getCourses() {
        return new String[]{"Art", "Math", "Music", "Science", "History", "Geography",
                "Business", "Family Studies", "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare"};
    }

    /**
     * @return an array of possible SERTs students could have
     */
    public String[] getSerts() {
        SinglyLinkedList<String> sertList = database.getSerts(); //get SERTs from database

        String[] sertArray = new String[sertList.size()]; //format it into an array
        for (int i = 0; i < sertList.size(); i++) {
            sertArray[i] = sertList.get(i);
        }

        return sertArray;
    }

    /**
     * @return a list of possible SERTs students could have
     */
    public ArrayList<String> getSertArrayList() {
        ArrayList<String> sertList = new ArrayList<>();

        for (String sert : database.getSerts()) { //get SERTs from database and format in list
            sertList.add(sert);
        }

        return sertList;
    }

    /**
     * Replaces the SERTs stored in the database with a new list of SERTs
     *
     * @param newSerts a new list of SERTs that will replace the previous one
     */
    public void setSerts(SinglyLinkedList<String> newSerts) {
        database.replaceSerts(newSerts);
    }

    /**
     * Creates an HTML report of the sessions that match the criteria specified by the various argument
     * These parameters are the same to that of {@link Query}.
     *
     * @param id           the id of the student the queried sessions reference
     * @param firstName    the first name of the student(s) the queried sessions reference
     * @param lastName     the last name of the student(s) the queried sessions reference
     * @param grade        the grade of the student(s) the queried sessions reference
     * @param earliestTime the earliest time queried sessions must start
     * @param latestTime   the latest time queried sessions must end
     * @param minTime      the minimum span of queried sessions
     * @param maxTime      the maximum span of queried sessions
     * @param reasons      a list of possible reasons the queried sessions must contain one of
     * @param serts        a list of possible serts the queried sessions must contain one of
     * @param courses      a list of possible courses the queried sessions must contain one of
     * @throws IOException thrown if the database fails to be accessed
     */
    public void generateHTML(int id, String firstName, String lastName, int grade, Timestamp earliestTime,
                             Timestamp latestTime, int minTime, int maxTime, SinglyLinkedList<String> reasons,
                             SinglyLinkedList<String> serts, SinglyLinkedList<String> courses) throws IOException {

        //convert the parameters into a Query object
        Query query = new Query(id, firstName, lastName, grade, earliestTime, latestTime, minTime, maxTime, reasons, serts, courses);

        SinglyLinkedList<Student> students = database.getStudents(); //list of students stored within the database
        SinglyLinkedList<Session> sessions = database.findSessions(query); //query the sessions

        HTMLWriter writer = new HTMLWriter(students, sessions); //write to the HTML
        writer.go();
    }

    /**
     * Creates an Excel report of the sessions that match the criteria specified by the various argument
     * These parameters are the same to that of {@link Query}.
     *
     * @param id           the id of the student the queried sessions reference
     * @param firstName    the first name of the student(s) the queried sessions reference
     * @param lastName     the last name of the student(s) the queried sessions reference
     * @param grade        the grade of the student(s) the queried sessions reference
     * @param earliestTime the earliest time queried sessions must start
     * @param latestTime   the latest time queried sessions must end
     * @param minTime      the minimum span of queried sessions
     * @param maxTime      the maximum span of queried sessions
     * @param reasons      a list of possible reasons the queried sessions must contain one of
     * @param serts        a list of possible serts the queried sessions must contain one of
     * @param courses      a list of possible courses the queried sessions must contain one of
     */
    public void generateExcel(int id, String firstName, String lastName, int grade, Timestamp earliestTime,
                              Timestamp latestTime, int minTime, int maxTime, SinglyLinkedList<String> reasons,
                              SinglyLinkedList<String> serts, SinglyLinkedList<String> courses) throws IOException {

        //convert the parameters into a Query object
        Query query = new Query(id, firstName, lastName, grade, earliestTime, latestTime, minTime, maxTime, reasons, serts, courses);

        SinglyLinkedList<Session> sessions = database.findSessions(query); //get all the sessions to display

        //create the workbook and sheet
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Log");

        //style used to format dates
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        XSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(workbook.createDataFormat().getFormat("m/d/yy h:mm"));

        //create the header row
        String[] header = {"Student id", "First Name", "Last Name", "Grade", "Sign-in Time",
                "Sign-out Time", "Reason", "SERT", "Course"};

        XSSFRow headerRow = sheet.createRow(0);
        for (int colIndex = 0; colIndex < header.length; colIndex++) {
            headerRow.createCell(colIndex).setCellValue(header[colIndex]);
        }

        int rowIndex = 1; //index of the row that is currently being written to
        for (Session session : sessions) { //iterate through all the sessions and write them one per row

            XSSFRow row = sheet.createRow(rowIndex);

            //write the session by creating the cells and setting their values and styles
            row.createCell(0).setCellValue(Utils.idToString(session.student.id));
            row.createCell(1).setCellValue(session.student.firstName);
            row.createCell(2).setCellValue(session.student.lastName);
            row.createCell(3).setCellValue(session.student.grade);
            row.createCell(4).setCellValue(df.format(session.startTime));
            row.createCell(5).setCellValue(df.format(session.endTime));
            row.createCell(6).setCellValue(session.reason);
            row.createCell(7).setCellValue(session.sert);
            row.createCell(8).setCellValue(session.course);

            row.getCell(4).setCellStyle(dateStyle);
            row.getCell(5).setCellStyle(dateStyle);

            rowIndex++;
        }

        //make it so that columns are auto-sized to the width of the longest entry
        for (int colIndex = 0; colIndex < header.length; colIndex++) {
            sheet.autoSizeColumn(colIndex);
        }

        try { //attempt to create the file
            File file = new File("Excel Files/Log.xlsx");

            //if the file exists, change its name so duplicates are avoided Log(1).xlsx, Log(2).xlsx...
            int counter = 1;
            while (!file.createNewFile()) {
                counter++;
                file = new File("Excel Files/Log(" + counter + ").xlsx");
            }

            //write the new file
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();

            //attempt to open the folder the file exists in
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File("Excel Files/"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the manager by closing the database
     */
    public void close() {
        database.close();
    }
}
