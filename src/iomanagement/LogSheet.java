package iomanagement;

public class LogSheet {

	private static final String[] header = {
		"Student Number", "First Name", "Last Name", "Date", "Sign-In Time", "Sign-Out Time", "Reason", "Subject Work", "Subject Missed"
	};

	private String filePath;

	public LogSheet(String filePath) {
	    this.filePath = filePath;
	}



}