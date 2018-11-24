package iomanagement;

public class ExcelManager {

    private static final ExcelManager DEFAULT_INSTANCE = new ExcelManager();

    public static ExcelManager getInstance() {
        return DEFAULT_INSTANCE;
    }

	private final String[] header = {
		"Student Number", "First Name", "Last Name", "Date", "Sign-In Time", "Sign-Out Time", "Reason", "Subject Work", "Subject Missed"
	};

	private ExcelManager() {
	}



}
