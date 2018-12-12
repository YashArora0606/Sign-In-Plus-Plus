package iomanagement;

import datamanagement.Student;
import exceptions.ImproperFormatException;
import utilities.SinglyLinkedList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Helper class that reads from a .xlsx file and generates a list of students
 * that are included in the excel file.
 *
 * @author Alston
 * last updated 12/5/2018
 */
public class StudentListReader {

    private static final String STUDENTS_FILE = "Student List/RHHSStudentList.csv"; //path of the student file

    /**
     * Returns a list of the students detailed by RHHSStudentList.csv
     * RHHSStudentList.csv must adhere to a specific format in order to be read without throwing an exception:
     * 1) the file must be found under the path "database/RHHSStudentList.csv"
     * 2) each row in the sheet, starting from the 2nd row, represents a student
     * 3) no empty or unfilled rows can exist in the sheet
     * 4) each row must contain (Student ID, First Name, Last Name, School Code, Grade, Home Room) in this order
     *
     * @return a list of students
     * @throws IOException             thrown if the .csv file cannot be found or opened
     * @throws ImproperFormatException thrown if the .csv file is improperly formatted
     */
    public static SinglyLinkedList<Student> getStudentList() throws IOException, ImproperFormatException {

        //list of students
        SinglyLinkedList<Student> students = new SinglyLinkedList<>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(STUDENTS_FILE)));

        reader.readLine(); //skip the header line

        String line = reader.readLine();
        while (line != null) {

            String[] entry = line.split(",");

            if (entry.length != 6) { //ensure an appropriate amount of columns are in each entry
                throw new ImproperFormatException();
            }

            int id = formatId(entry[0]);
            String firstName = formatName(entry[1]);
            String lastName = formatName(entry[2]);
            int grade = formatGrade(entry[4]);
            students.add(new Student(id, firstName, lastName, grade)); //read student from entry

            line = reader.readLine();
        }

        reader.close();

        return students;
    }

    /**
     * Coverts a string id into an integer id.
     * An id must be a numerical string with 9 or less characters
     *
     * @param id a string representation of a student id
     * @return an integer representation of the id argument
     * @throws ImproperFormatException thrown if the id argument cannot be formatted to an integer id
     */
    private static int formatId(String id) throws ImproperFormatException {

        int idNum;

        try {
            idNum = Integer.parseInt(id); //try to convert the string into an integer
        } catch (NullPointerException | NumberFormatException e) {
            throw new ImproperFormatException();
        }

        if (idNum < 0 || idNum > 999999999) { //check if id has 9 or less digits
            throw new ImproperFormatException();
        }

        return idNum;
    }

    /**
     * Formats a specified name by trimming it
     *
     * @param name a specified name
     * @return the formatted name
     */
    private static String formatName(String name) {

        name = name.trim();

        return name;
    }

    /**
     * Formats a string grade into an integer grade.
     * A grade must be a numerical string representing a value from 9 to 13 inclusive
     *
     * @param grade a string representation of a grade
     * @return an integer representation of the grade argument
     * @throws ImproperFormatException thrown if the grade argument cannot be formatted to an integer grade
     */
    private static int formatGrade(String grade) throws ImproperFormatException {

        int gradeNum;

        try {
            gradeNum = Integer.parseInt(grade); //try to convert the string into an integer
        } catch (NullPointerException | NumberFormatException e) {
            throw new ImproperFormatException();
        }

        if (gradeNum < 9 || gradeNum > 13) { //check if the grade is between 9 and 13 inclusive
            throw new ImproperFormatException();
        }

        return gradeNum;
    }

}