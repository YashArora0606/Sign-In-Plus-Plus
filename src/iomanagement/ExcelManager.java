package iomanagement;
import org.apache.poi.ss.usermodel.CellStyle; 
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import datamanagment.Session;
import utilities.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelManager {

    private File file;
	private XSSFWorkbook workbook;
	private XSSFSheet spreadsheet;

	private CellStyle stringStyle;
	private CellStyle dateStyle;
	private CellStyle timeStyle;
	
	private ArrayList<XSSFSheet> reasonSheets = new ArrayList<XSSFSheet>();

	private final String[] header = {
		"Student Number", "First Name", "Last Name", "Date", "Sign-In Time", "Sign-Out Time", "Teacher", "Reason"
	};
	
	private static String[] reasons = Utils.getReasons();


	public ExcelManager(String fileName) {
		try {
			file = new File(fileName);
			file.createNewFile();

			FileInputStream fs = new FileInputStream(file);
			workbook = new XSSFWorkbook(fs);
			spreadsheet = workbook.createSheet("Master");
			
			for (int i = 0; i < reasons.length; i++) {
				reasonSheets.add(workbook.createSheet(reasons[i]));
			}
			

            dateStyle = workbook.createCellStyle();
            dateStyle.setDataFormat((short)14);

            timeStyle = workbook.createCellStyle();
            timeStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("h:mm"));

            initializeSpreadsheet();

            fs.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeSpreadsheet() {
		XSSFRow titleRow = spreadsheet.createRow(0);
		for (int i = 0; i < header.length; i++) {
            titleRow.createCell(i).setCellValue(header[i]);
        }
	}

	public void logSession(Session session) {
        spreadsheet.shiftRows(1, Math.max(1, spreadsheet.getLastRowNum()), 1);

        XSSFRow newRow = spreadsheet.createRow(1);
        for (int i = 0; i< header.length; i++) {
            newRow.createCell(i);
        }

        newRow.getCell(0).setCellValue(session.student.id);
        
        newRow.getCell(1).setCellValue(session.student.firstName);
        
        newRow.getCell(2).setCellValue(session.student.lastName);
        
        newRow.getCell(3).setCellStyle(dateStyle);
        newRow.getCell(3).setCellValue(session.getStartTime());
        
        newRow.getCell(4).setCellStyle(timeStyle);
        newRow.getCell(4).setCellValue(session.getStartTime());
        
        newRow.getCell(5).setCellStyle(timeStyle);
        newRow.getCell(5).setCellValue(session.getEndTime());
        
        newRow.getCell(6).setCellValue(session.course);
        
        newRow.getCell(7).setCellValue(session.reason);
    }


	public void close() {
		
		try {
			
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(file.getName() + " written successfully");
	}
}
