import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Display extends JFrame {

	static JFrame window;
	JPanel uiPanel;
	int timesSubmitted = 0;

	public static void main(String[] args) throws IOException {
		window = new Display();
	}

	Display() throws IOException {
		
		// Create blank workbook
		XSSFWorkbook workbook = new XSSFWorkbook();
		// Create a blank sheet
		XSSFSheet spreadsheet = workbook.createSheet("Student Sign-In Sheet");	
		
		initializeSpreadsheet(workbook, spreadsheet);
		
		
		
		JButton signIn = new JButton("Sign-In");
		JButton signOut = new JButton("Sign-Out");

		JPanel panel = new JPanel();
		JPanel signInPanel = new JPanel();

		panel.add(signIn);
		panel.add(signOut);

		JTextField idField = new JTextField(8);
		idField.setFont(idField.getFont().deriveFont(50f));
		
		JButton closeProgram = new JButton("Close Program");


		JButton submitId = new JButton("Submit");
		signInPanel.add(idField);
		signInPanel.add(submitId);
		signInPanel.add(closeProgram);
		

		signIn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				panel.setVisible(false);
				signInPanel.setVisible(true);
				getContentPane().add(signInPanel);
			}

		});

		submitId.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				timesSubmitted++;
				signStudentIn(idField.getText(), timesSubmitted, spreadsheet);
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
		this.setTitle("Boi");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(200, 100);
		this.setSize(800, 600);
//		uiPanel = new uiPanel();
//		this.add(new uiPanel());
//		MyKeyListener keyListener = new MyKeyListener();
//		this.addKeyListener(keyListener);
//		this.requestFocusInWindow();
//		this.setResizable(true);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		

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

		//ArrayList<XSSFRow> rowList = new ArrayList<XSSFRow>();
		

	}
	
	public static void signStudentIn(String studentNum, int currentRow, XSSFSheet spreadsheet) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();  		
		String date = dtf.format(now).substring(0, dtf.format(now).indexOf(" "));
		String time = dtf.format(now).substring(dtf.format(now).indexOf(" ")+1, dtf.format(now).length()-3);
		if (Integer.parseInt(time.substring(0, 2)) > 12) {
			time = Integer.parseInt(time.substring(0, 2))%12 + time.substring(2, time.length()) + " PM";
		} else {
			time = time + " AM";
		}
		//rowList.add(spreadsheet.createRow(currentRow));
		spreadsheet.createRow(currentRow);
		spreadsheet.getRow(currentRow).createCell(0).setCellValue(studentNum);
		spreadsheet.getRow(currentRow).createCell(3).setCellValue(date);
		spreadsheet.getRow(currentRow).createCell(4).setCellValue(time);
	}

	private class uiPanel extends JPanel {
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			setDoubleBuffered(true);
			repaint();
		}
	}

}

//	// This is the inner class for the keyboard listener that detects key presses
//	// and runs the corresponding code
//	private class MyKeyListener implements KeyListener {
//
//		// These methods are mandatory and are not used
//		public void keyTyped(KeyEvent e) {
//		}
//
//		public void keyReleased(KeyEvent e) {
//		}
//
//		/**
//		 * keyPressed Method that does an action upon a key being pressed
//		 * 
//		 * @param a KeyEvent object of a key being pressed
//		 */
//		public void keyPressed(KeyEvent e) {
//
//			// If the space key is pressed
//			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//
//				// If the escape key is pressed
//			} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//
//				// The window closes
//				window.dispose();
//
//			}
//		}
//
//	} // End of MyKeyListener
//
//} // End of BounceTester