package iomanagement;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import datamanagment.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StudentListReader {

    private ArrayList<Student> students = new ArrayList<>();

    public StudentListReader(String fileName) {
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileInputStream inputStream = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet spreadsheet = workbook.getSheet("Sheet1");

            String id;
            String firstName;
            String lastName;
            String grade;
            for (int row = 1; row < spreadsheet.getLastRowNum() + 1; row++) {
                id = spreadsheet.getRow(row).getCell(0).getRawValue();
                for (int i = id.length(); i < 9; i++) {
                    id = "0" + id;
                }

                firstName = spreadsheet.getRow(row).getCell(1).toString();
                lastName = spreadsheet.getRow(row).getCell(2).toString();
                grade = spreadsheet.getRow(row).getCell(3).toString();
                students.add(new Student(firstName, lastName, id, grade));
            }

            inputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Student[] getStudents() {
        return students.toArray(new Student[0]);
    }
}
