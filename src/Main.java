import java.io.File; 
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Date;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("Student Sign-In Sheet");	

		
		XSSFRow titleRow = spreadsheet.createRow(0);
		titleRow.createCell(0).setCellValue("Student Number");
		titleRow.createCell(1).setCellValue("First Name");
		titleRow.createCell(2).setCellValue("Last Name");
		titleRow.createCell(3).setCellValue("Date");
		titleRow.createCell(4).setCellValue("Sign-In Time");
		titleRow.createCell(5).setCellValue("Sign-Out Time");

		ArrayList<XSSFRow> rowList = new ArrayList<XSSFRow>();
		
		// Find current date and time


		Scanner scan = new Scanner(System.in);
		boolean finished = false;
		int studentNum;
		String ans;
		int currentRow = 1;
		
		do {
			System.out.println("What is your student number?");
			studentNum = scan.nextInt();
			
			System.out.println("Exit the program? (Y/N)");
			scan.nextLine();
			ans = scan.nextLine();

			if (ans.toLowerCase().equals("y")) {
				finished = true;
			}
			
			signStudentIn(studentNum, currentRow, spreadsheet, rowList);

			currentRow++;
			
		} while (!finished);
		
		// Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File("Writesheet.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("Writesheet.xlsx written successfully");
	}
	
	public static void signStudentIn(int studentNum, int currentRow, XSSFSheet spreadsheet, ArrayList<XSSFRow> rowList) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();  		
		String date = dtf.format(now).substring(0, dtf.format(now).indexOf(" "));
		String time = dtf.format(now).substring(dtf.format(now).indexOf(" ")+1, dtf.format(now).length()-3);
		if (Integer.parseInt(time.substring(0, 2)) > 12) {
			time = Integer.parseInt(time.substring(0, 2))%12 + time.substring(2, time.length()) + " PM";
		} else {
			time = time + " AM";
		}
		rowList.add(spreadsheet.createRow(currentRow));
		spreadsheet.getRow(currentRow).createCell(0).setCellValue(studentNum);
		spreadsheet.getRow(currentRow).createCell(3).setCellValue(date);
		spreadsheet.getRow(currentRow).createCell(4).setCellValue(time);
	}
}