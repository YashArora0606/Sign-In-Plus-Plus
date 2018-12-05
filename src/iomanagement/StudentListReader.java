package iomanagement;

import datamanagement.Student;
import exceptions.ImproperFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.SinglyLinkedList;

import java.io.*;
import java.util.stream.Stream;


/**
 * Helper class that reads from a .xlsx file and generates a list of students
 * that are included in the excel file.
 *
 * @author Alston
 * last updated 12/3/2018
 */
public class StudentListReader {

    private static final String STUDENTS_FILE = "database/Students.xlsx"; //path of the student file

    /**
     * Returns a list of the students detailed by Students.xlsx
     * Students.xlsx must adhere to a specific format in order to be read without throwing an exception:
     * 1) the file must be found under the path "database/Students.xlsx"
     * 2) each row in the sheet, starting from the 2nd row, represents a student
     * 3) no empty or unfilled rows can exist in the sheet
     * 4) each row must contain (student id, first name, last name, grade) in this order
     *
     * @return a list of students
     * @throws IOException             thrown if the excel file cannot be found or opened
     * @throws ImproperFormatException thrown if the excel file is improperly formatted
     */
    /*
    public static SinglyLinkedList<Student> getStudents() throws IOException, ImproperFormatException {

        SinglyLinkedList<Student> students = new SinglyLinkedList<>();

        //open the .xlsx file into memory as a XSSFWorkbook
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(STUDENTS_FILE));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();

        XSSFSheet sheet = workbook.getSheetAt(0); //get first sheet in the file

        DataFormatter formatter = new DataFormatter();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) { //iterate through 2nd..last rows

            XSSFRow row = sheet.getRow(rowIndex); //make sure the row exists (is not null)
            if (sheet.getRow(rowIndex) == null) {
                throw new ImproperFormatException();
            }

            for (int colIndex = 0; colIndex < 4; colIndex++) { //make sure each cell in the row exists (is not null)
                if (row.getCell(colIndex) == null) {
                    throw new ImproperFormatException();
                }
            }

            //read and format the 4 cells (id, first name, last name, grade) in the row
            int id = formatId(formatter.formatCellValue(row.getCell(0)));
            String firstName = formatName(formatter.formatCellValue(row.getCell(1)));
            String lastName = formatName(formatter.formatCellValue(row.getCell(2)));
            int grade = formatGrade(formatter.formatCellValue(row.getCell(3)));

            students.add(new Student(id, firstName, lastName, grade)); //add the student to the list
        }

        return students;
    }
*/

    /**
     * Reads a .csv file and creates a list of students from it
     * @return a list of students
     * @throws IOException thrown if the file cannot be found or opened
     * @throws ImproperFormatException thrown if the data has been formatted improperly
     */
    public static SinglyLinkedList<Student> getStudentList() throws IOException, ImproperFormatException{
        SinglyLinkedList<Student> students = new SinglyLinkedList<>();

        BufferedReader input = new BufferedReader(new FileReader(new File("database/RHHSStudentList.csv")));
        String line;
        String[] data;

        input.readLine(); //reading the header line
        line = input.readLine();

        while (line != null) {
            data = line.split(",");
            students.add(new Student(formatId(data[0]), formatName(data[1]), formatName(data[2]), formatGrade(data[4])));
            line = input.readLine();
        }


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