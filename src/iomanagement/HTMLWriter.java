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
    SinglyLinkedList<Student> addedStudents;
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
                addToQueue();
            }







            inputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addToQueue() {
        boolean alreadyAdded = false;

        //adds current session to students list of sessions
        for (int i = 0; i < sessionList.size(); i++) {
            sessionList.get(i).student.addSession(sessionList.get(i)); //???? what the fuck
        }

        //filters out duplicate students and adds to an list of students
        for (int i = 0; i < sessionList.size(); i++) {
            alreadyAdded = false;
            if (addedStudents.size() == 0) {
                addedStudents.add(sessionList.get(i).student);

            } else {
                for (int j = 0; j < addedStudents.size(); j++) { //checks if the student has already been added to the list
                    if (sessionList.get(i).student.id.equals(addedStudents.get(j).id)) {
                        alreadyAdded = true;
                    }

                }
                if (!alreadyAdded) { // adds the student to the list if it hasn't been already
                    addedStudents.add(sessionList.get(i).student);
                }
            }


        }

        //adds the students in the list to a priority queue
        for (int i = 0; i < addedStudents.size(); i++) {
            studentList.add(addedStudents.get(i));
        }
    }
}
