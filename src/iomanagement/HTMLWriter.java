package iomanagement;

import datamanagement.Session;
import datamanagement.Student;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.SinglyLinkedList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Stack;

public class HTMLWriter {
    String excelFile;
    Stack<Session>[] studentSession;
    Student[] studentList;
    SinglyLinkedList<Session> sessionList;
    SinglyLinkedList<String> template;
    SinglyLinkedList<String> reportTemplate;

    /**
     * Constructor - initializes templates upon creation
     * @param excelFile
     * @param studentList
     */
    HTMLWriter(String excelFile, Student[] studentList, SinglyLinkedList<Session> sessionList){
        this.excelFile = excelFile;
        this.studentList = studentList;
        this.sessionList = sessionList;
        studentSession = new Stack[studentList.length];
        // initializing
        for (int i = 0; i < studentList.length; i++) {
            studentSession[i] = new Stack<>();
        }

        reportTemplate = getTemplate("HTMLStuff/ReportTemplate.txt");
        template = getTemplate("HTMLStuff/Template.txt");

    }

    public void go(){
        generateStack(sessionList);
        writeFile("HTMLStuff/report.html");
    }

    private void generateStack(SinglyLinkedList<Session> sessionList){
        for (int i = 0; i < sessionList.size(); i++){
            int index = findStudent(sessionList.get(i).student.id);
            studentSession[index].push(sessionList.get(i));
        }
    }


    private SinglyLinkedList<String> getTemplate(String fileName){
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        try {
            File myFile = new File(fileName);
            // Create a Scanner and associate it with the file
            Scanner input = new Scanner(myFile);

            while(input.hasNext()){
                String line = input.nextLine();
                list.add(line);
                System.out.println(line);
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * writeFile
     * writes to the html document
     * @param pathName the name of the document we are writing to
     */
    public void writeFile(String pathName){
        try {
            File myFile = new File(pathName);
            PrintWriter out = new PrintWriter(myFile);
            int modNum = 0;
            for (int i = 0; i < reportTemplate.size(); i++){
                if (!reportTemplate.get(i).equals("<!--insert-->")){
                    out.println(reportTemplate.get(i));
                } else {
                    switch (modNum) {
                        case 0:
                            for (int stuNum = 0; stuNum < studentList.length; stuNum++){
                                out.println("<a href=\"#"+studentList[stuNum].firstName+" "+studentList[stuNum].lastName+"\">Student 1</a><br/>");
                            }
                            modNum++;

                        case 1:
                            for (int stuNum = 0; stuNum < studentSession.length; stuNum++){
                                outputStudent(out, stuNum);
                                writeStudentGraph(out, stuNum);
                            }
                            modNum++; //if we want the overall graph
                        case 2:
                            writeOverallGraph(out);

                    }
                }
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writeOverallGraph
     * writes the graph for all the sign in info
     * @param out printwriter to write to file
     */
    private void writeOverallGraph (PrintWriter out) {
        int[] percentageList = calculateOverallPercentages();
        String[] id = {"Test", "ChillZone", "QuietWork", "GroupWork", "AcademicSupport","Total"};
        String[] displayText = {"Test", "Chill Zone", "Quiet Work", "Group Work", "Academic Support","Total"};

        out.println("<div id=\"graph\">");
        out.print("<table id = ");
        out.print( "\"s-graph\" </table>");
        out.println("<caption> Overall Graph </caption>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        for (int i = 0; i < id.length; i++){
            out.println("<tr class=\"reason\" id=\""+id[i]+"\">");
            out.println("<th scope=\"row\">" + displayText[i] + " </th>");
            out.println("<td class=\"" +  displayText[i] +"\" style=\"height: " + percentageList[i] * 10+ "px\"><p> " + percentageList[i] + " </p></td>");
            out.println("</tr>");
        }


    }


    /**
     * writeStudentGraph
     * writes the specific student's sign in graph
     * @param out printwriter to write to file
     * @param index student's index in the the array
     */
    private void writeStudentGraph (PrintWriter out, int index) {
        int[] percentageList = calculateStudentPercentage(index);
        String[] id = {"Test", "ChillZone", "QuietWork", "GroupWork", "AcademicSupport","Total"};
        String[] displayText = {"Test", "Chill Zone", "Quiet Work", "Group Work", "Academic Support","Total"};

        out.println("<div id=\"graph\">");
        out.print("<table id = ");
        out.print( "\"s-graph\" </table>");
        out.println("<caption> " + studentSession[index].get(0).student.firstName + " " +
                studentSession[index].get(0).student.lastName + " Graph </caption>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        for (int i = 0; i < id.length; i++){
            out.println("<tr class=\"reason\" id=\""+id[i]+"\">");
            out.println("<th scope=\"row\">" + displayText[i] + " </th>");
            out.println("<td class=\"" +  displayText[i] +"\" style=\"height: " + percentageList[i] * 10+ "px\"><p> " + percentageList[i] + " </p></td>");
            out.println("</tr>");
        }

//        // Test reason bar
//        out.println("<tr class=\"reason\" id=\"Test\">");
//        out.println("<th scope=\"row\"> Test </th>");
//        out.println("<td class=\"Test bar\" style=\"height: " + percentageList[0] * 10+ "px\"><p> " + percentageList[0] + " </p></td>");
//        out.println("</tr>");

//        //chill zone reason bar
//        out.println("<tr class=\"reason\" id=\"ChillZone\">");
//        out.println("<th scope=\"row\"> Chill Zone </th>");
//        out.println("<td class=\"ChillZone bar\" style=\"height: " + percentageList[1] * 10+ "px\"><p> " + percentageList[1] + " </p></td>");
//        out.println("</tr>");
//
//        //quiet work reason bar
//        out.println("<tr class=\"reason\" id=\"QuietWork\">");
//        out.println("<th scope=\"row\"> Quiet Work </th>");
//        out.println("<td class=\"QuietWork bar\" style=\"height: " + percentageList[2] * 10 + "px\"><p> " + percentageList[2] + " </p></td>");
//        out.println("</tr>");
//
//        //group work reason bar
//        out.println("<tr class=\"reason\" id=\"GroupWork\">");
//        out.println("<th scope=\"row\"> Group Work </th>");
//        out.println("<td class=\"GroupWork bar\" style=\"height: " + percentageList[3] * 10+ "px\"><p> " + percentageList[3] + " </p></td>");
//        out.println("</tr>");
//
//        //Academic Support bar
//        out.println("<tr class=\"reason\" id=\"AcademicSupport\">");
//        out.println("<th scope=\"row\"> Academic Support</th>");
//        out.println("<td class=\"AcademicSupport bar\" style=\"height: " + percentageList[4] * 10+ "px\"><p> " + percentageList[4] + " </p></td>");
//        out.println("</tr>");
//
//        //Total bar
//        out.println("<tr class=\"reason\" id=\"Total\">");
//        out.println("<th scope=\"row\"> Total </th>");
//        out.println("<td class=\"Total bar\" style=\"height: " + percentageList[5] * 10+ "px\"><p> " + percentageList[5] + " </p></td>");
//        out.println("</tr>");
        out.println("</tbody");
        out.println("</table>");
        out.println("<div id=\"ticks\">");
        out.println("<div class=\"tick\" style=\"height: 59px;\"><p></p></div>");
        out.println("<div class=\"tick\" style=\"height: 59px;\"><p></p></div>");
        out.println("<div class=\"tick\" style=\"height: 59px;\"><p></p></div>");
        out.println("<div class=\"tick\" style=\"height: 59px;\"><p></p></div>");
        out.println("</div>");
        out.println("</div>");


    }

    /**
     * calculateOverallPercentages
     * Calculates how many times each reason was used for sign in over all the students
     * @return integer array of each reason's sum
     */
    private int[] calculateOverallPercentages () {
        int[] percentageArray = new int[6];
        int testNum = 0;
        int czNum = 0;
        int qwNum = 0;
        int gwNum = 0;
        int asNum = 0;
        int total = 0;
        for (int i =0; i < studentSession.length; i++) {
            for (int j = 0; j < studentSession[i].size(); j++) {
                if (studentSession[i].get(j).reason.equals("Test")) {
                    testNum++;
                } else if (studentSession[i].get(j).reason.equals("Chill Zone")) {
                    czNum++;
                } else if (studentSession[i].get(j).reason.equals("Academic Support")) {
                    asNum++;
                } else if (studentSession[i].get(j).reason.equals("Quiet Work")) {
                    qwNum++;
                } else if (studentSession[i].get(j).reason.equals("Group Work")) {
                    gwNum++;
                }
                total++;
            }
        }
        percentageArray[0] = testNum;
        percentageArray[1] = czNum;
        percentageArray[2] = qwNum;
        percentageArray[3] = gwNum;
        percentageArray[4] = asNum;
        percentageArray[5] = total;
        return percentageArray;
    }

    /**
     * calculateStudentPercentage
     * calculates how many times each reason was used for sign in for a specific student
     * @param index position of student in array
     * @return integer array of sum of each reason
     */
    private int[] calculateStudentPercentage(int index) {
        int[] percentageArray = new int[6];
        int testNum = 0;
        int czNum = 0;
        int qwNum = 0;
        int gwNum = 0;
        int asNum = 0;
        int total = 0;
        for (int i = 0; i < studentSession[index].size(); i++) {
            if (studentSession[i].get(i).reason.equals("Test")) {
                testNum++;
            } else if (studentSession[index].get(i).reason.equals("Chill Zone")) {
                czNum++;
            } else if (studentSession[index].get(i).reason.equals("Academic Support")) {
                asNum++;
            } else if (studentSession[index].get(i).reason.equals("Quiet Work")) {
                qwNum++;
            } else if (studentSession[index].get(i).reason.equals("Group Work")) {
                gwNum++;
            }
            total++;
        }
        percentageArray[0] = testNum;
        percentageArray[1] = czNum;
        percentageArray[2] = qwNum;
        percentageArray[3] = gwNum;
        percentageArray[4] = asNum;
        percentageArray[5] = total;
        return percentageArray;
    }

    /**
     * outputStudent
     * Outputs each student's session into the html document
     * @param out printwriter to write to file
     * @param index position of student in array
     */
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

                    case 1:
                        out.println("<h2>" + studentList[index].firstName + " " + studentList[index].lastName + "</h2>");
                        modNum++;

                    case 2:
                        for (int i = 0; i < studentSession[index].size(); i++) {
                            out.println("  <tr>");
                            out.println("    <th>" + studentSession[index].get(i).startTime);
                            out.println("    <th>" + studentSession[index].get(i).endTime);
                            out.println("    <th>" + studentSession[index].get(i).reason);
                            out.println("    <th>" + studentSession[index].get(i).courseWork);
                            out.println("    <th>" + studentSession[index].get(i).courseMissed);
                            out.println("  <tr>");
                        }
                }
            }
        }
        out.println();
    }

    /**
     * createDate
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
     *  findStudentById()
     *  Method that finds a student object based on their id
     *  @param id id that is individual to each student, calling another findStudentById() method to find a student based on id
     *  @return Student that is the student object based on the student number
     */
    private int findStudent(int id) {
        return findStudent(id, 0, studentList.length - 1);
    }

    /**
     *  findStudentById()
     *  Method that finds a student object based on their id and some recursive variables
     *  @param id student id that is individual to each student and recursively passed in
     *  @param low int low that is the lowest id length
     *  @param high int high that is the highest id length
     *  @return Student that is the student object based on the student number
     */
    private int findStudent(int id, int low, int high) {
//      System.out.println(id + ", " + low + ", " + high);
        if (high >= low) {
            int mid = (low + high)/2;

            if (id == studentList[mid].id) {
                return mid;
            } else if (id < studentList[mid].id) {
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
