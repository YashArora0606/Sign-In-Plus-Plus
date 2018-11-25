package iomanagement;

import datamanagment.Student;
import display.ErrorWindow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import utilities.ExcelUtils;
import utilities.Queue;
import utilities.SinglyLinkedList;
import utilities.Utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Reads from an .xlsx file of students
 * 11/25/2018
 * @author Alston
 */
public class StudentListReader {

    private final String STUDENTS_DIR = "database/";
    private final String STUDENTS_FILE = "Students.xlsx";

    private final String[] header = {"Student ID", "First Name", "Last Name", "Grade"};

    private ArrayList<Student> students = new ArrayList<>();

    /**
     * Constructs the reader and reads from the file
     */
    public StudentListReader() {

        try {

            //load the .xlsx file
            XSSFWorkbook workbook = ExcelUtils.loadFile(STUDENTS_DIR + STUDENTS_FILE);

            //open first sheet (by default, an excel file must contain at least 1 sheet)
            XSSFSheet sheet = workbook.getSheetAt(0);

            //create cell styles
            XSSFCellStyle redCell = workbook.createCellStyle();
            redCell.setFillForegroundColor(IndexedColors.RED.getIndex());
            redCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFCellStyle defaultCell = workbook.createCellStyle();
            defaultCell.setFillPattern(FillPatternType.NO_FILL);

            //read file
            Queue<Cell> flaggedCells = new SinglyLinkedList<>(); //queue of cells that are improperly formatted
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

                XSSFRow row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow (rowIndex);
                }

                String[] fields = new String[4];
                for (int col = 0; col < fields.length; col++) {
                    XSSFCell cell = row.getCell(col);
                    if (cell == null) {
                        cell = row.createCell(col);
                    }

                    cell.setCellStyle(defaultCell);

                    String value = ExcelUtils.getValueFromCell(cell).trim();
                    if (!isValidField(value, col)) {
                        flaggedCells.add(cell);
                    }
                    fields[col] = value;
                }
                
                students.add(new Student(fields[0], fields[1], fields[2], fields[3]));
            }

            if (flaggedCells.size() > 0) {
                while (flaggedCells.size() > 0) {
                    flaggedCells.poll().setCellStyle(redCell);
                }

                ExcelUtils.saveFile(workbook, STUDENTS_DIR + STUDENTS_FILE);

                throw new FileFormatException();
            }

            ExcelUtils.saveFile(workbook, STUDENTS_DIR + STUDENTS_FILE);

            // What happens when a file cannot be found
        } catch (FileNotFoundException e) {
            new ErrorWindow("Students.xlsx could not be found and was auto-generated");
            createNewStudentFile();
            openDirectory();
            System.exit(1);

        // Improperly formatted excel document
        } catch (FileFormatException e) {
            new ErrorWindow("Students.xlsx not properly formatted");
            openDirectory();
            System.exit(1);

            // What happens when an unknown error occurs
        } catch (IOException e) {
            new ErrorWindow("Unknown error in Students.xlsx");
            System.exit(1);

        }
    }

    /**
     * @return an array of Student objects, sorted by ID
     */
    public Student[] getStudents() {
        Student[] studentArray = students.toArray(new Student[0]);
        Arrays.sort(studentArray);
        return studentArray;
    }

    /**
     * Creates a new Students.xlsx file
     */
    private void createNewStudentFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Student List");

        XSSFRow row = sheet.createRow(0);
        for (int i = 0; i < header.length; i++) {
            row.createCell(i).setCellValue(header[i]);
            sheet.autoSizeColumn(i);
        }

        try {
            ExcelUtils.saveFile(workbook, STUDENTS_DIR + STUDENTS_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the directory to the Students.xlsx file on the desktop
     */
    private void openDirectory() {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(new File(STUDENTS_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validates a String field given its column
     * column = 0 -> ID column
     * column = 1 -> First name column
     * column = 2 -> Last name column
     * column = 3 -> Grade column
     * @param field
     * @param column
     * @return if the field is valid
     */
    private boolean isValidField(String field, int column) {
        switch (column) {
            case 0:
                return isValidId(field);

            case 1:
                return isValidName(field);

            case 2:
                return isValidName(field);

            case 3:
                return isValidGrade(field);

            default:
                throw new IndexOutOfBoundsException();
        }
    }


    /**
     * Checks if a String is a valid representation of a student ID
     * A student ID must be an integer that is 9 numbers long (leading zeroes allowed)
     * @param id
     * @return true, if the string is a valid representation of a student ID
     */
    private boolean isValidId(String id) {
        return (id.length() == 9 && Utils.isAnInteger(id));
    }


    /**
     * Checks if a String is a valid representation of a name
     * A name must be at least 1 character, contain no digits, and have its first character capitalized
     * @param name
     * @return true, if the string is a valid representation of a name
     */
    private boolean isValidName(String name) {
        return ((name.length() >= 1) &&
                (name.matches("^[^0-9]+$")) &&
                (name.charAt(0) == Character.toUpperCase(name.charAt(0))));
    }


    /**
     * Checks if a String validly represents a grade
     * A grade must be an integer from 9 - 13
     * @param grade
     * @return true, if the String is a valid representation of a grade
     */
    private boolean isValidGrade(String grade) {
        if (Utils.isAnInteger(grade)) {
            int gradeNum = Integer.parseInt(grade);
            return (gradeNum >= 9 && gradeNum <= 13);
        }
        return false;
    }
}
