import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelManager {

	String fileName;
	int timesSubmitted;
	XSSFWorkbook workbook;
	XSSFSheet spreadsheet;

	ExcelManager(String fileName) {
		this.fileName = fileName;
		File file = new File(fileName);
		FileInputStream fs;
		try {
			fs = new FileInputStream(file);
			workbook = new XSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		spreadsheet = workbook.getSheet("SignInSheet");
		initializeSpreadsheet();
		int nextRowNumber = spreadsheet.getLastRowNum();
		timesSubmitted = nextRowNumber;
	}

	public void initializeSpreadsheet() {
		XSSFRow titleRow = spreadsheet.createRow(0);
		titleRow.createCell(0).setCellValue("Student Number");
		titleRow.createCell(1).setCellValue("First Name");
		titleRow.createCell(2).setCellValue("Last Name");
		titleRow.createCell(3).setCellValue("Date");
		titleRow.createCell(4).setCellValue("Sign-In Time");
		titleRow.createCell(5).setCellValue("Sign-Out Time");
		titleRow.createCell(6).setCellValue("Teacher");
		titleRow.createCell(7).setCellValue("Reason");

		//signUnsignedPeopleOut();
	}

//	private void signUnsignedPeopleOut() {
//		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime latestTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute)
//		now.isAfter(latestTime);
//
//		for (int i = 0; i < spreadsheet.getLastRowNum() + 1; i++) {
//			
//		}
//	}

	public void signStudentIn(String studentNum, String subject, String reason) {

		timesSubmitted++;
		
		int currentRow = timesSubmitted;

		spreadsheet.createRow(currentRow);
		spreadsheet.getRow(currentRow).createCell(0).setCellValue(studentNum);
		spreadsheet.getRow(currentRow).createCell(3).setCellValue(getCurrentDateFormatted());
		spreadsheet.getRow(currentRow).createCell(4).setCellValue(getCurrentTimeFormatted());
		spreadsheet.getRow(currentRow).createCell(6).setCellValue(subject);
		spreadsheet.getRow(currentRow).createCell(7).setCellValue(reason);

	}

	public boolean signStudentOut(String studentNum) {
		int currentRow = -1;

		for (int i = 0; i < spreadsheet.getLastRowNum() + 1; i++) {

			boolean alreadySignedOut = true;

			try {
				spreadsheet.getRow(i).getCell(5).getStringCellValue();
			} catch (NullPointerException e) {
				alreadySignedOut = false;
			}

			if (spreadsheet.getRow(i).getCell(0).getStringCellValue().equals(studentNum.toString())
					&& !alreadySignedOut) {
				currentRow = i;
			}
		}

		if (currentRow == -1) {
			return false;
		} else {
			spreadsheet.getRow(currentRow).createCell(5).setCellValue(getCurrentTimeFormatted());
			return true;
		}
	}

	public String getCurrentTimeFormatted() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now).substring(0, dtf.format(now).indexOf(" "));
		String time = dtf.format(now).substring(dtf.format(now).indexOf(" ") + 1, dtf.format(now).length() - 3);
		if (Integer.parseInt(time.substring(0, 2)) > 12) {
			time = Integer.parseInt(time.substring(0, 2)) % 12 + time.substring(2, time.length()) + " PM";
		} else {
			time = time + " AM";
		}
		return time;
	}

	public String getCurrentDateFormatted() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now).substring(0, dtf.format(now).indexOf(" "));
		return date;
	}

	public void closeExcelFile() throws IOException {
		FileOutputStream out = new FileOutputStream(new File(fileName));
		workbook.write(out);
		out.close();
		System.out.println(fileName + " written successfully");
	}
}
