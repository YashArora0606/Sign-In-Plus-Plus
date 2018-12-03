package datamanagement;

import exceptions.AlreadyLoggedInException;
import exceptions.ImproperFormatException;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import exceptions.StudentAlreadyExistsException;
import exceptions.StudentDoesNotExistException;
import iomanagement.StudentListReader;
import utilities.SinglyLinkedList;
import utilities.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SignInManager {

	public final static String[] reasons = new String[] { "Test", "Chill Zone", "Quiet Work", "Academic Help",
			"Group Work" };

	public static String[] serts;

	public final static String[] courses = new String[] { "Art", "Math", "Music", "Science", "History", "Geography",
			"Business", "Family Studies", "Physical Ed.", "Tech Studies", "Social Sciences", "Lunch / Spare" };

	public static ArrayList<String> sertList = new ArrayList<String>();

	private Database database;

	public SignInManager(Database database) {
		this.database = database;
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("SERTList.txt"));

			String line;

			while ((line = in.readLine()) != null) {
				sertList.add(line);
			}

			updateSerts(sertList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void updateSerts(ArrayList<String> newList) {
		sertList = newList;
		serts = new String[sertList.size()];
		for (int i = 0; i < sertList.size(); i++) {
			serts[i] = sertList.get(i);
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

		SinglyLinkedList<Student> newStudents = StudentListReader.getStudents();
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
			throws InvalidIdException, AlreadyLoggedInException {

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
				throw new AlreadyLoggedInException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Session session = new Session(student, Utils.getNow(), null, reason, sert, course);
		return database.addSession(session);
	}

	public boolean signOut(int id) throws InvalidIdException, NotLoggedInException {

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
				throw new NotLoggedInException();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return database.resolveOpenSessions(id, Utils.getNow());
	}

	public void generateHTML(int id, Timestamp earliestDate, Timestamp latestDate, int minTime, int maxTime,
			SinglyLinkedList<String> reasons, SinglyLinkedList<String> serts, SinglyLinkedList<String> courses)
			throws IOException {

	}

	public void generateExcel(int id, Timestamp earliestDate, Timestamp latestDate, int minTime, int maxTime,
			SinglyLinkedList<String> reasons, SinglyLinkedList<String> serts, SinglyLinkedList<String> courses)
			throws IOException {

		Query query = new Query(id, earliestDate, latestDate, minTime, maxTime, reasons, serts, courses);

		SinglyLinkedList<Session> sessions = database.findSessions(query);

	}

	public void close() {
		database.close();
	}

	public static ArrayList<String> getSertList() {
		return sertList;
	}

}
