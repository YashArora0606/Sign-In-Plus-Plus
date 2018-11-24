package iomanagement;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;

public class ExcelManager {

    private static final ExcelManager DEFAULT_INSTANCE = new ExcelManager();

    public static ExcelManager getInstance() {
        return DEFAULT_INSTANCE;
    }

    private File file;
	private XSSFWorkbook workbook;
	private XSSFSheet master;

	private CellStyle dateStyle;
	private CellStyle timeStyle;

	private final String[] header = {
		"Student Number", "First Name", "Last Name", "Date", "Sign-In Time", "Sign-Out Time", "Reason", "Subject Work", "Subject Missed"
	};

	public ExcelManager() {
	}


}