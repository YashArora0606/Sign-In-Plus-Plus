package iomanagement;

import datamanagement.Session;
import datamanagement.Student;
import utilities.SinglyLinkedList;
import utilities.Stack;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class HTMLWriter {
    private Stack<Session>[] studentSession;
    private SinglyLinkedList<Student> studentList;
    private SinglyLinkedList<Session> sessionList;
    private SinglyLinkedList<String> template;
    private SinglyLinkedList<String> reportTemplate;

    /**
     * Constructor - initializes templates upon creation
     *
     * @param studentList
     */
    public HTMLWriter(SinglyLinkedList<Student> studentList, SinglyLinkedList<Session> sessionList) {
        this.studentList = studentList;
        this.sessionList = sessionList;
        studentSession = new Stack[studentList.size()];
        // initializing
        for (int i = 0; i < studentList.size(); i++) {
            studentSession[i] = new Stack<>();
        }

        reportTemplate = getTemplate("HTMLStuff/ReportTemplate.txt");
        template = getTemplate("HTMLStuff/Template.txt");

    }

    public void go() {
        generateStack(sessionList);
        writeFile(determineFileName());
    }

    private void generateStack(SinglyLinkedList<Session> sessionList) {
        for (Session session : sessionList) {
            int index = studentList.indexOf(session.student);
            studentSession[index].push(session);
        }
    }


    private SinglyLinkedList<String> getTemplate(String fileName) {
        SinglyLinkedList<String> list = new SinglyLinkedList<>();
        try {
            File myFile = new File(fileName);
            // Create a Scanner and associate it with the file
            Scanner input = new Scanner(myFile);

            while (input.hasNext()) {
                String line = input.nextLine();
                list.add(line);
            }

            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String determineFileName() {
        File myFile = new File("Reports/report.html");
        if (myFile.exists()) {
            int counter = 1;
            myFile = new File("Reports/report (" + counter + ").html");
            while (myFile.exists()) {
                counter++;
                myFile = new File("Reports/report (" + counter + ").html");
            }
            return "Reports/report (" + counter + ").html";
        } else {
            return "Reports/report.html";
        }
    }

    /**
     * writeFile
     * writes to the html document
     *
     * @param pathName the name of the document we are writing to
     */
    private void writeFile(String pathName) {
        try {
            File myFile = new File(pathName);
            PrintWriter out = new PrintWriter(myFile);
            int modNum = 0;
            for (int i = 0; i < reportTemplate.size(); i++) {
                if (!reportTemplate.get(i).equals("insert")) {
                    out.println(reportTemplate.get(i));
                } else {
                    switch (modNum) {
                        case 0:
                            for (int stuNum = 0; stuNum < studentList.size(); stuNum++) {
                                out.println("      <a class=\"mdl-navigation__link\" href=\"#" +
                                        studentList.get(stuNum).firstName + studentList.get(stuNum).lastName + "\">" +
                                        studentList.get(stuNum).firstName + " " + studentList.get(stuNum).lastName + "</a>");
                            }
                            out.println("      <a class=\"mdl-navigation__link\" href=\"#OverallGraph\">Overall Graph</a>");
                            modNum++;
                            break;

                        case 1:
                            for (int stuNum = 0; stuNum < studentSession.length; stuNum++) {
                                out.println("<div id=\"student\">");
                                outputStudent(out, stuNum);
                                writeStudentGraph(out, stuNum);
                                out.println("</div>");
                                out.println();
                            }
                            modNum++; //if we want the overall graph
                            break;

                        case 2:
                            writeOverallGraph(out);
                            break;

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
     *
     * @param out printwriter to write to file
     */
    private void writeOverallGraph(PrintWriter out) {
        int[] percentageList = calculateOverallPercentages();
        String[] id = {"Test", "ChillZone", "QuietWork", "GroupWork", "AcademicSupport", "Total"};
        String[] displayText = {"Test", "Chill Zone", "Quiet Work", "Group Work", "Academic Support", "Total"};

        out.println("<a name=\"OverallGraph\"> </a>");
        out.println("<h2>Overall Graph</h2>");
        out.println("<div id=\"graph\">");
        out.print("<table id = ");
        out.print("\"s-graph\" </table>");
        out.println("<caption> Overall Graph </caption>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        for (int i = 0; i < id.length; i++) {
            out.println("<tr class=\"reason\" id=\"" + id[i] + "\">");
            out.println("<th scope=\"row\">" + displayText[i] + " </th>");
            out.println("<td class=\"" + id[i] + " bar" + "\" style=\"height: " + percentageList[i] * 10 + "px\"><p> " + percentageList[i] + " </p></td>");
            out.println("</tr>");
        }

        out.println("</tbody>");
        out.println("</table>");
        out.println("<div id=\"ticks\">");
        for (int i = 0; i < 5; i++) {
            out.println("<div class=\"tick\" style=\"height: 50px;\"><p></p></div>");
        }
        out.println("</div>");
        out.println("</div>");


    }


    /**
     * writeStudentGraph
     * writes the specific student's sign in graph
     *
     * @param out   printwriter to write to file
     * @param index student's index in the the array
     */
    private void writeStudentGraph(PrintWriter out, int index) {
        int[] percentageList = calculateStudentPercentage(index);
        String[] id = {"Test", "ChillZone", "QuietWork", "GroupWork", "AcademicSupport", "Total"};
        String[] displayText = {"Test", "Chill Zone", "Quiet Work", "Group Work", "Academic Support", "Total"};

        out.println("<div id=\"graph\">");
        out.print("<table id = ");
        out.print("\"s-graph\" </table>");
        out.println("<caption> " + studentList.get(index).firstName + " " +
                studentList.get(index).lastName + " Graph </caption>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th></th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        for (int i = 0; i < id.length; i++) {
            out.println("<tr class=\"reason\" id=\"" + id[i] + "\">");
            out.println("<th scope=\"row\">" + displayText[i] + " </th>");
            out.println("<td class=\"" + id[i] + " bar" + "\" style=\"height: " + percentageList[i] * 10 + "px\"><p> " + percentageList[i] + " </p></td>");
            out.println("</tr>");
        }

        out.println("</tbody>");
        out.println("</table>");
        out.println("<div id=\"ticks\">");
        for (int i = 0; i < 5; i++) {
            out.println("<div class=\"tick\" style=\"height: 50px;\"><p></p></div>");
        }
        out.println("</div>");
        out.println("</div>");


    }

    /**
     * calculateOverallPercentages
     * Calculates how many times each reason was used for sign in over all the students
     *
     * @return integer array of each reason's sum
     */
    private int[] calculateOverallPercentages() {
        int[] percentageArray = new int[6];
        int testNum = 0;
        int czNum = 0;
        int qwNum = 0;
        int gwNum = 0;
        int asNum = 0;
        int total = 0;
        for (int i = 0; i < studentSession.length; i++) {
            for (int j = 0; j < studentSession[i].size(); j++) {
                String reason = studentSession[i].get(j).reason;
                total++;
                switch (reason) {
                    case ("Test"):
                        testNum++;
                        break;

                    case ("Chill Zone"):
                        czNum++;
                        break;

                    case ("Academic Support"):
                        asNum++;
                        break;

                    case ("Quiet Work"):
                        qwNum++;
                        break;

                    case ("Group Work"):
                        gwNum++;
                        break;
                }
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
     *
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
            if (studentSession[index].get(i).reason.equals("Test")) {
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
     *
     * @param out   printwriter to write to file
     * @param index position of student in array
     */
    private void outputStudent(PrintWriter out, int index) {
        out.println("<div id=\"studentTable\">");
        out.println("<a name=" + studentList.get(index).firstName + studentList.get(index).lastName + "></a>");
        out.println("<h2>" + studentList.get(index).firstName + " " + studentList.get(index).lastName + "</h2>");

        for (int j = 0; j < template.size(); j++) {
            if (!template.get(j).equals("insert")) {
                out.println(template.get(j));
            } else {
                for (int i = 0; i < studentSession[index].size(); i++) {
                    out.println("    <tr>");
                    out.println("      <td class=\"mdl-data-table__cell--non-numeric\">" + studentSession[index].get(i).startTime + "</td>");
                    out.println("      <td class=\"mdl-data-table__cell--non-numeric\">" + studentSession[index].get(i).endTime + "</td>");
                    out.println("      <td class=\"mdl-data-table__cell--non-numeric\">" + studentSession[index].get(i).reason + "</td>");
                    out.println("      <td class=\"mdl-data-table__cell--non-numeric\">" + studentSession[index].get(i).sert + "</td>");
                    out.println("      <td class=\"mdl-data-table__cell--non-numeric\">" + studentSession[index].get(i).course + "</td>");
                    out.println("    <tr>");
                }
                break;
            }
        }
        out.println("</div>");
        out.println();
    }

    /**
     * createDate
     *
     * @param date
     * @return
     */
    private Date createDate(String date) {
        int year;
        int month;
        int day;
        int hour;
        int minute;
        int second;

        year = Integer.parseInt(date.substring(0, date.indexOf("-")));
        date = date.substring(date.indexOf("-") + 1);
        month = Integer.parseInt(date.substring(0, date.indexOf("-")));
        date = date.substring(date.indexOf("-") + 1);
        day = Integer.parseInt(date.substring(0, date.indexOf(" ")));
        date = date.substring(date.indexOf(" ") + 1);
        hour = Integer.parseInt(date.substring(0, date.indexOf(":")));
        date = date.substring(date.indexOf(":") + 1);
        minute = Integer.parseInt(date.substring(0, date.indexOf(":")));
        date = date.substring(date.indexOf(":") + 1);
        second = Integer.parseInt(date);

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, minute, second);
        return cal.getTime();
    }
}
