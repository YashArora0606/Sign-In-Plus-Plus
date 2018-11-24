package iomanagement;

import datamanagment.Student;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Reads from the list of students
 */
public class StudentListReader {

    private ArrayList<Student> students = new ArrayList<>();

    public StudentListReader(String fileName) {

        try {
            FileInputStream inputStream = new FileInputStream(fileName);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);

            for (int row = 1; row < spreadsheet.getLastRowNum() + 1; row++) {
                String id = formatId(spreadsheet.getRow(row).getCell(0).getRawValue());
                String firstName = formatId(spreadsheet.getRow(row).getCell(1).toString());
                String lastName = formatId(spreadsheet.getRow(row).getCell(2).toString());
                String grade = formatId(spreadsheet.getRow(row).getCell(3).toString());
                students.add(new Student(firstName, lastName, id, grade));
            }

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Student[] getStudents() {
        Student[] studentArray = students.toArray(new Student[0]);
        Arrays.sort(studentArray);
        return studentArray;
    }

    private String formatId(String id) {
        for (int i = id.length(); i < 9; i++) {
            id = "0" + id;
        }
        return id;
    }

    private String formatName(String name) {
        return name;
    }

    private String formatGrade(String grade) {
        return grade;
    }
}
