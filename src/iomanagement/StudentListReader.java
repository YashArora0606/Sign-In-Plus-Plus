package iomanagement;

import datamanagment.Student;
import display.ErrorWindow;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        XSSFWorkbook workbook = null;

        try {
            workbook = ExcelUtils.loadFile(STUDENTS_DIR + STUDENTS_FILE);

        } catch (FileNotFoundException e) {
            new ErrorWindow("Students.xlsx could not be found and was auto-generated");
            createNewStudentFile();
            openDirectory();
            System.exit(1);

        } catch (IOException e) {
            new ErrorWindow("Unknown error in Students.xlsx");
            System.exit(1);
        }

        //open first sheet (by default, an excel file must contain at least 1 sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        //create cell styles
        XSSFCellStyle redCell = workbook.createCellStyle();
        redCell.setFillForegroundColor(IndexedColors.RED.getIndex());
        redCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFCellStyle defaultCell = workbook.createCellStyle();
        defaultCell.setFillPattern(FillPatternType.NO_FILL);

        //read file
        boolean properlyFormatted = true;
        Queue<String[]> fieldQueue = new SinglyLinkedList<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            XSSFRow row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            String[] fields = new String[4];
            for (int col = 0; col < fields.length; col++) {
                XSSFCell cell = row.getCell(col);
                if (cell == null) {
                    cell = row.createCell(col);
                }

                String value = ExcelUtils.getValueFromCell(cell).trim();
                if (!isValidField(value, col)) {
                    properlyFormatted = false;
                    cell.setCellStyle(redCell);
                } else {
                    cell.setCellStyle(defaultCell);
                }

                fields[col] = value;
            }

            fieldQueue.add(fields);
        }

        try {
            ExcelUtils.saveFile(workbook, STUDENTS_DIR + STUDENTS_FILE);
        } catch (IOException e) {
            new ErrorWindow("Unknown error in Students.xlsx");
            System.exit(1);
        }

        if (!properlyFormatted || fieldQueue.size() == 0) {
            new ErrorWindow("Students.xlsx improperly formatted");
            openDirectory();
            System.exit(1);
        }

        while (fieldQueue.size() > 0) {
            String[] fields = fieldQueue.poll();
            students.add(new Student(fields[1], fields[2], fields[0], fields[3]));
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
                return Utils.isValidId(field);

            case 1:
                return Utils.isValidName(field);

            case 2:
                return Utils.isValidName(field);

            case 3:
                return Utils.isValidGrade(field);

            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
