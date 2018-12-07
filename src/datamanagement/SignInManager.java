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
import java.util.ArrayList;

public class SignInManager {

	private Database database;

    public SignInManager(Database database) {
		this.database = database;

		try { // make a button for this later
			configureStudents();
		} catch (IOException | ImproperFormatException e) {
			e.printStackTrace();
		}
	}

	public boolean addStudent(int id, String firstName, String lastName, int grade)
			throws StudentAlreadyExistsException {

		Student existingStudent;

		try {
			existingStudent = database.findStudentById(id);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (existingStudent != null) {
			throw new StudentAlreadyExistsException();
		}

		Student newStudent = new Student(id, firstName, lastName, grade);
		return database.addStudent(newStudent);
	}

	public void configureStudents() throws IOException, ImproperFormatException {

		SinglyLinkedList<Student> newStudents = StudentListReader.getStudentList();
		SinglyLinkedList<Student> currStudents = database.getStudents();

		for (Student newStudent : newStudents) {
			if (currStudents.indexOf(newStudent) == -1) {
				database.addStudent(newStudent);
			} else {
				database.updateStudent(newStudent);
			}
		}

		for (Student currStudent : currStudents) {
			if (newStudents.indexOf(currStudent) == -1) {
				database.removeStudentById(currStudent.id);
			}
		}
	}

	public boolean removeStudent(int id) throws StudentDoesNotExistException {
		Student existingStudent;

		try {
			existingStudent = database.findStudentById(id);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (existingStudent == null) {
			throw new StudentDoesNotExistException();
		}

		return database.removeStudentById(id);
	}

	public boolean signIn(int id, String reason, String sert, String course)
			throws InvalidIdException, AlreadySignedInException {

		// check if student exists
		Student student;

		try {
			student = database.findStudentById(id);
		} catch (IOException e) {
			return false;
		}

		if (student == null) {
			throw new InvalidIdException(id);
		}

		try {
			if (database.isStudentSignedIn(id)) {
				throw new AlreadySignedInException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Session session = new Session(student, Utils.getNow(), null, reason, sert, course);
		return database.addSession(session);
	}

	public boolean signOut(int id) throws InvalidIdException, NotSignedInException {

		Student student;

		try {
			student = database.findStudentById(id);
		} catch (IOException e) {
			return false;
		}

		if (student == null) {
			throw new InvalidIdException(id);
		}

		try {
			if (!database.isStudentSignedIn(id)) {
				throw new NotSignedInException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return database.resolveOpenSessions(id, Utils.getNow());
	}


	public String[] getReasons() {
        return new String[] {"Test", "Chill Zone", "Quiet Work", "Academic Help", "Group Work"};
    }

    public String[] getCourses() {
        return new String[] { "Art", "Math", "Music", "Science", "History", "Geography",
                "Business", "Family Studies", "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare" };
    }

    public String[] getSerts() {
        SinglyLinkedList<String> sertList = database.getSerts();

        String[] sertArray = new String[sertList.size()];
        for (int i = 0; i < sertList.size(); i++) {
            sertArray[i] = sertList.get(i);
        }

        return sertArray;
    }

    public ArrayList<String> getSertList() {
        ArrayList<String> sertList = new ArrayList<>();

        for (String sert : database.getSerts()) {
            sertList.add(sert);
        }

        return sertList;
    }

    public void setSerts(SinglyLinkedList<String> newSerts) {
        database.updateSerts(newSerts);
    }

    public void generateHTML(int id, Timestamp earliestDate, Timestamp latestDate, int minTime, int maxTime,
			SinglyLinkedList<String> reasons, SinglyLinkedList<String> serts, SinglyLinkedList<String> courses)
			throws IOException {

		Query query = new Query(id, earliestDate, latestDate, minTime, maxTime, reasons, serts, courses);

		SinglyLinkedList<Student> students = database.getStudents();
		SinglyLinkedList<Session> sessions = database.findSessions(query);

		HTMLWriter writer = new HTMLWriter(students, sessions);
		writer.go();
	}

	public void generateExcel(int id, Timestamp earliestDate, Timestamp latestDate, int minTime, int maxTime,
			SinglyLinkedList<String> reasons, SinglyLinkedList<String> serts, SinglyLinkedList<String> courses)
			throws IOException {

		Query query = new Query(id, earliestDate, latestDate, minTime, maxTime, reasons, serts, courses);

		SinglyLinkedList<Session> sessions = database.findSessions(query);

		XSSFWorkbook workbook = new XSSFWorkbook();

		XSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("m/d/yy h:mm"));

		XSSFSheet sheet = workbook.createSheet("Log");

		String[] header = { "Student id", "First Name", "Last Name", "Grade", "Sign-in Time", "Sign-out Time", "Reason",
				"SERT", "Course" };

		XSSFRow headerRow = sheet.createRow(0);
		for (int colIndex = 0; colIndex < header.length; colIndex++) {
			headerRow.createCell(colIndex).setCellValue(header[colIndex]);
			sheet.autoSizeColumn(colIndex);
		}

		int rowIndex = 1;
		for (Session session : sessions) {

			XSSFRow row = sheet.createRow(rowIndex);

			row.createCell(0).setCellValue(Utils.idToString(session.student.id));
			row.createCell(1).setCellValue(session.student.firstName);
			row.createCell(2).setCellValue(session.student.lastName);
			row.createCell(3).setCellValue(session.student.grade);
			row.createCell(4).setCellStyle(dateStyle);
			row.getCell(4).setCellValue(session.startTime);
			row.createCell(5).setCellStyle(dateStyle);
			row.getCell(5).setCellValue(session.endTime);
			row.createCell(6).setCellValue(session.reason);
			row.createCell(7).setCellValue(session.sert);
			row.createCell(8).setCellValue(session.course);

			rowIndex++;

		}

		try {
			File file = new File("database/Log.xlsx");

			int counter = 1;
			while (!file.createNewFile()) {
				counter++;
				file = new File("database/Log(" + counter + ").xlsx");
			}

			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(new File("database/"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		database.close();
	}
}
