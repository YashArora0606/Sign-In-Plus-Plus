import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Display extends JFrame {

	static JFrame window;
	JPanel uiPanel;
	int timesSubmitted = 0;
	String FILE_NAME = "Writesheet.xlsx";

	public static void main(String[] args) throws IOException {
		window = new Display();
	}

	Display() throws IOException {

		
		File file = new File(FILE_NAME);
		FileInputStream fs = new FileInputStream(file);
		XSSFWorkbook workbook = new XSSFWorkbook(fs);
		XSSFSheet spreadsheet = workbook.getSheet("SignInSheet");
		
		int nextRowNumber = spreadsheet.getLastRowNum();
		timesSubmitted = nextRowNumber;

		initializeSpreadsheet(workbook, spreadsheet);

		JButton signIn = new JButton("Sign-In");
		JButton signOut = new JButton("Sign-Out");
		JButton closeProgram = new JButton("Close Program");
		JButton submitId = new JButton("Sign In");
		JButton submitIdOut = new JButton("Sign Out");

		JButton back = new JButton("Back");
		JButton backOut = new JButton("Back");
		
		JPanel panel = new JPanel();
		JPanel signInPanel = new JPanel();
		JPanel signOutPanel = new JPanel();
		
		JTextField idField = new JTextField(10);
		idField.setText("Student Number");
			
		JComboBox reasons = new JComboBox();
		reasons.addItem("Select Reason");
		reasons.addItem("Test");
		reasons.addItem("Chill Zone");
		reasons.addItem("Quiet Work");
		reasons.addItem("Academic Support");
		reasons.addItem("Group Work");
		
		JTextField idFieldOut = new JTextField(10);
		JTextField teacherField = new JTextField(10);
		teacherField.setText("Mr. Mangat");

		idField.setFont(idField.getFont().deriveFont(35f));
		idFieldOut.setFont(idField.getFont().deriveFont(35f));

		panel.add(signIn);
		panel.add(signOut);
		panel.add(closeProgram);

		signInPanel.add(idField);
		signInPanel.add(reasons);
		signInPanel.add(teacherField);
		signInPanel.add(submitId);

		signInPanel.add(back);
		
		signOutPanel.add(idFieldOut);
		signOutPanel.add(submitIdOut);
		signOutPanel.add(backOut);


		signIn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(false);
				signInPanel.setVisible(true);
				getContentPane().add(signInPanel);
			}

		});

		signOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(false);
				signOutPanel.setVisible(true);
				getContentPane().add(signOutPanel);
			}

		});
		
		back.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(true);
				signInPanel.setVisible(false);
			}

		});
		
		backOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(true);
				signOutPanel.setVisible(false);
			}

		});

		submitId.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				//if (text.length() == 9 && isNumeric(text)) {
					timesSubmitted++;
					signStudentIn(idField.getText(), timesSubmitted, spreadsheet, teacherField.getText(), (String) reasons.getSelectedItem());
					idField.setText(null);
				//}

			}

		});
		
		submitIdOut.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {

				String text = idFieldOut.getText();

				//if (text.length() == 9 && isNumeric(text)) {

					signStudentOut(text, spreadsheet);
					idFieldOut.setText(null);
				//}

			}

		});

		closeProgram.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				try {
					endSpreadsheet(workbook);
				} catch (IOException e) {
					e.printStackTrace();
				}
				window.dispose();
			}

		});

		this.getContentPane().add(panel);
		this.setTitle("SignIn++");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(200, 100);
		this.setSize(1000, 600);
		this.setVisible(true);

	}

	public static boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public static void endSpreadsheet(XSSFWorkbook workbook) throws IOException {
		// Write the workbook in file system
		FileOutputStream out = new FileOutputStream(new File("Writesheet.xlsx"));
		workbook.write(out);
		out.close();
		System.out.println("Writesheet.xlsx written successfully");
	}

	public static void initializeSpreadsheet(XSSFWorkbook workbook, XSSFSheet spreadsheet) throws IOException {
		XSSFRow titleRow = spreadsheet.createRow(0);
		titleRow.createCell(0).setCellValue("Student Number");
		titleRow.createCell(1).setCellValue("First Name");
		titleRow.createCell(2).setCellValue("Last Name");
		titleRow.createCell(3).setCellValue("Date");
		titleRow.createCell(4).setCellValue("Sign-In Time");
		titleRow.createCell(5).setCellValue("Sign-Out Time");
		titleRow.createCell(6).setCellValue("Teacher");
		titleRow.createCell(7).setCellValue("Reason");
	}

	public static void signStudentIn(String studentNum, int currentRow, XSSFSheet spreadsheet, String teacher, String reason) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now).substring(0, dtf.format(now).indexOf(" "));
		String time = dtf.format(now).substring(dtf.format(now).indexOf(" ") + 1, dtf.format(now).length() - 3);
		if (Integer.parseInt(time.substring(0, 2)) > 12) {
			time = Integer.parseInt(time.substring(0, 2)) % 12 + time.substring(2, time.length()) + " PM";
		} else {
			time = time + " AM";
		}
		spreadsheet.createRow(currentRow);
		spreadsheet.getRow(currentRow).createCell(0).setCellValue(studentNum);
		spreadsheet.getRow(currentRow).createCell(3).setCellValue(date);
		spreadsheet.getRow(currentRow).createCell(4).setCellValue(time);
		spreadsheet.getRow(currentRow).createCell(6).setCellValue(teacher);
		spreadsheet.getRow(currentRow).createCell(7).setCellValue(reason);

	}
	
	public static void signStudentOut(String studentNum, XSSFSheet spreadsheet) {
		
		int currentRow = -1;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String date = dtf.format(now).substring(0, dtf.format(now).indexOf(" "));
		String time = dtf.format(now).substring(dtf.format(now).indexOf(" ") + 1, dtf.format(now).length() - 3);
		if (Integer.parseInt(time.substring(0, 2)) > 12) {
			time = Integer.parseInt(time.substring(0, 2)) % 12 + time.substring(2, time.length()) + " PM";
		} else {
			time = time + " AM";
		}
		
		for (int i = 0; i < spreadsheet.getLastRowNum() + 1; i++) {
			
			//System.out.println(spreadsheet.getRow(i).getCell(0).getStringCellValue());
			//System.out.println(studentNum.toString());
			boolean alreadySignedOut = true;
			try {
				spreadsheet.getRow(i).getCell(5).getStringCellValue();
			} catch (NullPointerException e) {
				alreadySignedOut = false;
			}
			
			if (spreadsheet.getRow(i).getCell(0).getStringCellValue().equals(studentNum.toString()) && !alreadySignedOut) {
				currentRow = i;
			}
		}
		
		if (currentRow == -1) {
			System.out.print("Does not exist");
		} else {
			spreadsheet.getRow(currentRow).createCell(5).setCellValue(time);
		}
		
		
	}

	private class uiPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);
			repaint();
		}
	}

}