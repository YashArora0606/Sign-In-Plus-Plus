package iomanagement;

import datamanagment.Student;
import display.ErrorWindow;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Reads from the list of students
 */
public class StudentListReader {

    //private final String STUDENTS_DIR = "database/";
    private final String STUDENTS_DIR = "";
    private final String STUDENTS_FILE = "StudentList.xlsx";
    private final String[] header = {"Student ID", "First Name", "Last Name", "Grade"};

    private ArrayList<Student> students = new ArrayList<>();

    public StudentListReader() {

        try {
            FileInputStream inputStream = new FileInputStream(STUDENTS_DIR + STUDENTS_FILE);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);

            for (int row = 1; row < spreadsheet.getLastRowNum() + 1; row++) {
                String id = spreadsheet.getRow(row).getCell(0).getRawValue();
                String firstName = spreadsheet.getRow(row).getCell(1).toString();
                String lastName = spreadsheet.getRow(row).getCell(2).toString();
                String grade = spreadsheet.getRow(row).getCell(3).toString();
                students.add(new Student(firstName, lastName, id, grade));
            }

            inputStream.close();

        // What happens when a file cannot be found
        } catch (FileNotFoundException e) {

            createNewStudentFile();

            new ErrorWindow("File containing Students could not be found");

            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(new File(STUDENTS_DIR));
            } catch (IOException b) {
                b.printStackTrace();
            }

            System.exit(1);

            // What happens when an unknown error occurs
        } catch (IOException e) {

        }
    }

    public Student[] getStudents() {
        Student[] studentArray = students.toArray(new Student[0]);
        Arrays.sort(studentArray);
        return studentArray;
    }

    private void createNewStudentFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Student List");

        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            row.createCell(i).setCellValue(header[i]);
            sheet.autoSizeColumn(i);
        }

        try {
            File file = new File(STUDENTS_DIR + STUDENTS_FILE);
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
