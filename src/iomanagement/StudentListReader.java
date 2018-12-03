package iomanagement;

import datamanagement.Student;
import exceptions.ImproperFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.SinglyLinkedList;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * Reads from an .xlsx file of students
 * 11/25/2018
 *
 * @author Alston
 */
public class StudentListReader {

    private static final String STUDENTS_FILE = "database/Students.xlsx";

    public static SinglyLinkedList<Student> getStudents() throws IOException, ImproperFormatException {

        DataFormatter formatter = new DataFormatter();

        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(STUDENTS_FILE));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        inputStream.close();

        XSSFSheet sheet = workbook.getSheetAt(0);

        SinglyLinkedList<Student> students = new SinglyLinkedList<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            XSSFRow row = sheet.getRow(rowIndex);
            if (sheet.getRow(rowIndex) == null) {
                throw new ImproperFormatException();
            }

            for (int colIndex = 0; colIndex < 4; colIndex++) {
                if (row.getCell(colIndex) == null) {
                    throw new ImproperFormatException();
                }
            }

            int id = formatId(formatter.formatCellValue(row.getCell(0)));
            String firstName = formatName(formatter.formatCellValue(row.getCell(1)));
            String lastName = formatName(formatter.formatCellValue(row.getCell(2)));
            int grade = formatGrade(formatter.formatCellValue(row.getCell(3)));
            students.add(new Student(id, firstName, lastName, grade));
        }

        return students;
    }


    private static int formatId(String id) throws ImproperFormatException {

        int idNum;

        try {
            idNum = Integer.parseInt(id);
        } catch (NullPointerException | NumberFormatException e) {
            throw new ImproperFormatException();
        }

        if (idNum < 0 || idNum > 999999999) {
            throw new ImproperFormatException();
        }

        return idNum;
    }

    private static String formatName(String name) {

        name = name.trim();

        return name;
    }

    private static int formatGrade(String grade) throws ImproperFormatException {

        int gradeNum;

        try {
            gradeNum = Integer.parseInt(grade);
        } catch (NullPointerException | NumberFormatException e) {
            throw new ImproperFormatException();
        }

        if (gradeNum < 9 || gradeNum > 13) {
            throw new ImproperFormatException();
        }

        return gradeNum;
    }


}