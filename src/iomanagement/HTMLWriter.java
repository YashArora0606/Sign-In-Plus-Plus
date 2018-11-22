package iomanagement;

import datamanagment.Session;
import datamanagment.Student;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.SinglyLinkedList;
import utilities.SlowPriorityQueue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HTMLWriter {
    String excelFile;
    SlowPriorityQueue<Student> studentList;
    SinglyLinkedList<Session> sessionList;
    HTMLWriter(String excelFile){
        this.excelFile = excelFile;
    }

    public void go(){

    }

    private void read() {
        try {
            File file = new File(excelFile);
            file.createNewFile();
            FileInputStream inputStream = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet spreadsheet = workbook.getSheet("Sheet1");

            String id;
            String firstName;
            String lastName;
            String grade;
            String reason;
            String subjectWork;
            String courseMissed;
            for (int row = 1; row < spreadsheet.getLastRowNum() + 1; row++) {
                id = spreadsheet.getRow(row).getCell(0).getRawValue();
                for (int i = id.length(); i < 9; i++) {
                    id = "0" + id;
                }

                firstName = spreadsheet.getRow(row).getCell(1).toString();
                lastName = spreadsheet.getRow(row).getCell(2).toString();
                grade = spreadsheet.getRow(row).getCell(3).toString();
                reason = spreadsheet.getRow(row).getCell(8).toString();
                subjectWork = spreadsheet.getRow(row).getCell(6).toString();
                courseMissed = spreadsheet.getRow(row).getCell(5).toString();

                sessionList.add(new Session(new Student(firstName, lastName, id, grade), courseMissed, reason, subjectWork));
            }

            inputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
