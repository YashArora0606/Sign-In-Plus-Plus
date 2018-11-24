package iomanagement;

import datamanagment.Session;
import datamanagment.Student;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.SinglyLinkedList;
import utilities.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HTMLWriter {
    String excelFile;
    Stack<Session>[] studentSession;
    Student[] studentList;
    SinglyLinkedList<String> template;
    SinglyLinkedList<String> reportTemplate;

    HTMLWriter(String excelFile, Student[] studentList){
        this.excelFile = excelFile;
        this.studentList = studentList;
        studentSession = new Stack[studentList.length];
        // initializing
        for (int i = 0; i < studentList.length; i++) {
            studentSession[i] = new Stack<>();
        }

        try {
            File myFile = new File("HTMLStuff/Template.txt");
            // Create a Scanner and associate it with the file
            Scanner input = new Scanner(myFile);

            while(input.hasNext()){
                String line = input.nextLine();
                template.add(line);
                System.out.println(line);
            }

            input.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void go(){
        read();
        write();
    }

    private void read() {
        try {
            File file = new File(excelFile);
            file.createNewFile();
            FileInputStream inputStream = new FileInputStream(file);

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet spreadsheet = workbook.getSheet("Sheet1");

            String id;
            Date timeIn;
            Date timeOut;
            String reason;
            String subjectWork;
            String courseMissed;
            for (int row = 1; row < spreadsheet.getLastRowNum() + 1; row++) {
                id = spreadsheet.getRow(row).getCell(0).getRawValue();
                timeIn = createDate(spreadsheet.getRow(row).getCell(4).getRawValue());
                timeOut = createDate(spreadsheet.getRow(row).getCell(5).getRawValue());
                reason = spreadsheet.getRow(row).getCell(6).toString();
                subjectWork = spreadsheet.getRow(row).getCell(7).toString();
                courseMissed = spreadsheet.getRow(row).getCell(8).toString();


                int index = findStudent(id);
                studentSession[index].push(new Session(courseMissed, reason, subjectWork,timeIn, timeOut));
            }

            inputStream.close();
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(){

    }

    private void processReportTemplate(){
        try {
            File myFile = new File("HTMLStuff/ReportTemplate.txt");
            // Create a Scanner and associate it with the file
            Scanner input = new Scanner(myFile);

            while(input.hasNext()){
                String line = input.nextLine();
                reportTemplate.add(line);
                System.out.println(line);
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(){
        try {
            File myFile = new File("HTMLStuff/report.html");
            PrintWriter out = new PrintWriter(myFile);
            int modNum = 0;
            for (int i = 0; i < reportTemplate.size(); i++){
                if (!reportTemplate.get(i).equals("<!--insert-->")){
                    out.println(reportTemplate.get(i));
                } else {
                    switch (modNum) {
                        case (0):
                            for (int stuNum = 0; stuNum < studentList.length; stuNum++){
                                out.println("<a href=\"#"+studentList[stuNum].firstName+" "+studentList[stuNum].lastName+"\">Student 1</a><br/>");
                            }
                            modNum++;
                            return;

                        case (1):
                           for (int stuNum = 0; stuNum < studentSession.length; stuNum++){
                               outputStudent(out, stuNum);
                           }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void outputStudent(PrintWriter out, int index) {
        int modNum = 0;
        for (int j = 0; j < template.size(); j++) {
            if (!template.get(j).equals("insert")){
                out.println(template.get(j));
            } else {
                switch (modNum) {
                    case 0:
                        out.println("<a name=" + studentList[index].firstName + " " + studentList[index].lastName + "></a>");
                        modNum++;
                        return;

                    case 1:
                        out.println("<h2>" + studentList[index].firstName + " " + studentList[index].lastName + "</h2>");
                        modNum++;
                        return;

                    case 2:
                        for (int i = 0; i < studentSession[index].size(); i++) {
                            out.println("  <tr>");
                            out.println("    <th>" + studentSession[index].get(i).getStartTime());
                            out.println("    <th>" + studentSession[index].get(i).getEndTime());
                            out.println("    <th>" + studentSession[index].get(i).reason);
                            out.println("    <th>" + studentSession[index].get(i).subjectWork);
                            out.println("    <th>" + studentSession[index].get(i).courseMissed);
                            out.println("  <tr>");
                        }
                        return;
                }
            }
        }
        out.println();
    }

    /**
     *
     * @param date
     * @return
     */
    private Date createDate(String date){
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        year = Integer.parseInt(date.substring(0,date.indexOf("-")));
        date = date.substring(date.indexOf("-")+1);
        month = Integer.parseInt(date.substring(0,date.indexOf("-")));
        date = date.substring(date.indexOf("-")+1);
        day = Integer.parseInt(date.substring(0,date.indexOf(" ")));
        date = date.substring(date.indexOf(" ")+1);
        hour = Integer.parseInt(date.substring(0,date.indexOf(":")));
        date = date.substring(date.indexOf(":")+1);
        minute = Integer.parseInt(date.substring(0,date.indexOf(":")));
        date = date.substring(date.indexOf(":")+1);
        second = Integer.parseInt(date);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month-1, day, hour, minute, second);
        return cal.getTime();
    }


    /**
     *  findStudent()
     *  Method that finds a student object based on their id
     *  @param id id that is individual to each student, calling another findStudent() method to find a student based on id
     *  @return Student that is the student object based on the student number
     */
    private int findStudent(String id) {
        if (id == null || id.length() != 9 || !Utils.isAnInteger(id)) {
            return -1;
        }
        return findStudent(id, 0, studentList.length - 1);
    }

    /**
     *  findStudent()
     *  Method that finds a student object based on their id and some recursive variables
     *  @param id student id that is individual to each student and recursively passed in
     *  @param low int low that is the lowest id length
     *  @param high int high that is the highest id length
     *  @return Student that is the student object based on the student number
     */
    private int findStudent(String id, int low, int high) {
//      System.out.println(id + ", " + low + ", " + high);
        if (high >= low) {
            int mid = (low + high)/2;

            if (id.compareTo(studentList[mid].id) == 0) {
                return mid;
            } else if (id.compareTo(studentList[mid].id) < 0) {
                return findStudent(id, low, mid-1);
            } else {
                return findStudent(id, mid+1, high);
            }
        }

        return -1;
    }

    /**private void addToQueue() {
        boolean alreadyAdded;
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
    }**/
}
