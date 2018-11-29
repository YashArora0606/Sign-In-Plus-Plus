package iomanagement;

import datamanagement.Student;
import display.ErrorWindow;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilities.ExcelUtils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


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
        boolean fileProperlyFormatted = true;

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {

            XSSFRow row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }


        }

        try {
            ExcelUtils.saveFile(workbook, STUDENTS_DIR + STUDENTS_FILE);
        } catch (IOException e) {
            new ErrorWindow("Unknown error in Students.xlsx");
            System.exit(1);
        }

        if (!fileProperlyFormatted) {
            new ErrorWindow("Students.xlsx improperly formatted");
            openDirectory();
            System.exit(1);
        }
    }

    public Student[] getStudents() {
        return students.toArray(new Student[0]);
    }

    /**
     * Creates a new Students.xlsx file
     */
    private void createNewStudentFile() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Student List");;

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

    private boolean isValidId(int id) {
        return (id >= 0 && id <= 999999999);
    }

    private boolean isValidGrade(int grade) {
        return (grade >= 9 && grade <= 13);
    }
}