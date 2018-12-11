/**
 * [GenerateSheetPanel.java]
 * The panel from the dashboard which allows user to filter certain aspects to generate the excel file
 * December 2 2018
 */

package display.panels;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import display.customcomponents.CustomTextField;
import display.customcomponents.DropDownMenu;
import display.customcomponents.SelectMultipleMenu;
import iomanagement.HTMLWriter;
import utilities.SinglyLinkedList;
import utilities.Utils;

public class GenerateSheetPanel extends JPanel {
	private Window display;
	private SignInManager signInManager;
	private JPanel panel;

	private int maxX;
	private int maxY;

	private CustomButton back;
	private CustomButton generateExcel;
	private CustomButton generateHTML;

	private SelectMultipleMenu reasonSelect;
	private SelectMultipleMenu sertSelect;
	private SelectMultipleMenu courseMissingSelect;
	private DropDownMenu gradeSelect;

	private CustomTextField idField;
	private CustomTextField earliestDateField;
	private CustomTextField latestDateField;
	private CustomTextField firstNameField;
	private CustomTextField lastNameField;
	private CustomTextField maxTimeField;
	private CustomTextField minTimeField;

	private String errorMessage = "";
	private static final int PADDING_CONSTANT = Utils.scale(20);

	/**
	 * Constructor Initializes and positions all text fields and drop down menus
	 * 
	 * @param display Window object to which this panel belongs
	 */
	public GenerateSheetPanel(Window display, SignInManager signInManager) {
		this.display = display;
		this.signInManager = signInManager;
		this.panel = this;
		this.maxX = display.maxX;
		this.maxY = display.maxY;

		this.setBackground(Utils.colours[4]);
		this.setLayout(new BorderLayout()); // Border layout used to organize the several different components on the
											// screen

		JLayeredPane pane = new JLayeredPane(); // layered pane allows items to be placed on different level as the
												// graphics
		pane.setPreferredSize(new Dimension(maxX, maxY));

		reasonSelect = new SelectMultipleMenu(signInManager.getReasons(), "Reason");
		sertSelect = new SelectMultipleMenu(signInManager.getSerts(), "SERT");
		courseMissingSelect = new SelectMultipleMenu(signInManager.getCourses(), "Course Missing");
		gradeSelect = new SelectMultipleMenu(new String[] { "9", "10", "11", "12" }, "Grade");

		// setting bounds for the dropdown menus
		reasonSelect.setBounds(maxX / 2 - reasonSelect.getPreferredSize().width * 2 - PADDING_CONSTANT * 2,
				Utils.scale(300), reasonSelect.getPreferredSize().width, reasonSelect.getPreferredSize().height);

		sertSelect.setBounds(maxX / 2 - sertSelect.getPreferredSize().width - PADDING_CONSTANT, Utils.scale(300),
				sertSelect.getPreferredSize().width, sertSelect.getPreferredSize().height);

		courseMissingSelect.setBounds(maxX / 2 + PADDING_CONSTANT, Utils.scale(300),
				courseMissingSelect.getPreferredSize().width, courseMissingSelect.getPreferredSize().height);

		gradeSelect.setBounds(maxX / 2 + gradeSelect.getPreferredSize().width + PADDING_CONSTANT * 2, Utils.scale(300),
				sertSelect.getPreferredSize().width, sertSelect.getPreferredSize().height);

		pane.add(reasonSelect);
		pane.add(sertSelect);
		pane.add(courseMissingSelect);
		pane.add(gradeSelect);

		// all the fields
		idField = new CustomTextField("Student Id"); // takes in student id (if teacher wants only that student's info
		idField.setBounds(maxX / 2 - idField.getPreferredSize().width / 2, PADDING_CONSTANT,
				idField.getPreferredSize().width, idField.getPreferredSize().height);

		firstNameField = new CustomTextField("First Name"); // takes in the student's name (if teacher wants only
															// students of that first name)
		firstNameField.setBounds(maxX / 2 - firstNameField.getPreferredSize().width / 2,
				idField.getBounds().y + idField.getPreferredSize().height + PADDING_CONSTANT,
				firstNameField.getPreferredSize().width, firstNameField.getPreferredSize().height);

		lastNameField = new CustomTextField("Last Name"); // takes in the student's last name (if teacher wants data of
															// only students with that last name)
		lastNameField.setBounds(maxX / 2 - lastNameField.getPreferredSize().width / 2,
				firstNameField.getBounds().y + firstNameField.getPreferredSize().height + PADDING_CONSTANT,
				lastNameField.getPreferredSize().width, lastNameField.getPreferredSize().height);

		earliestDateField = new CustomTextField("Earliest Date (DD/MM/YYYY)"); // takes in the earliest date to filter
																				// out older entries
		earliestDateField.setBounds(
				(int) (maxX / 2 - firstNameField.getPreferredSize().width / 2
						- earliestDateField.getPreferredSize().getWidth() - PADDING_CONSTANT),
				idField.getBounds().y + idField.getPreferredSize().height + PADDING_CONSTANT,
				firstNameField.getPreferredSize().width, firstNameField.getPreferredSize().height);

		latestDateField = new CustomTextField("Latest Date (DD/MM/YYYY)"); // takes in the latest date to filter out
																			// newer entries
		latestDateField.setBounds(
				(int) (maxX / 2 - firstNameField.getPreferredSize().width / 2
						- latestDateField.getPreferredSize().getWidth() - PADDING_CONSTANT),
				firstNameField.getBounds().y + firstNameField.getPreferredSize().height + PADDING_CONSTANT,
				lastNameField.getPreferredSize().width, lastNameField.getPreferredSize().height);

		minTimeField = new CustomTextField("Min Time (Minutes)"); // filters sessions of a minimum certain duration
		minTimeField.setBounds((int) (maxX / 2 + firstNameField.getPreferredSize().width / 2 + PADDING_CONSTANT),
				idField.getBounds().y + idField.getPreferredSize().height + PADDING_CONSTANT,
				firstNameField.getPreferredSize().width, firstNameField.getPreferredSize().height);

		maxTimeField = new CustomTextField("Max Time (Minutes)"); // filters session of a maximum certain duration
		maxTimeField.setBounds((int) (maxX / 2 + firstNameField.getPreferredSize().width / 2 + PADDING_CONSTANT),
				firstNameField.getBounds().y + firstNameField.getPreferredSize().height + PADDING_CONSTANT,
				lastNameField.getPreferredSize().width, lastNameField.getPreferredSize().height);

		pane.add(idField);
		pane.add(firstNameField);
		pane.add(lastNameField);
		pane.add(earliestDateField);
		pane.add(latestDateField);
		pane.add(minTimeField);
		pane.add(maxTimeField);

		pane.setBackground(null);

		add(pane);

		this.addMouseListener(new MyMouseListener());

		this.setVisible(true);
	}

	/**
	 * Method to paint components on the panel
	 * 
	 * @param g Graphics object to display visuals
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// back button leads to teacher dashboard
		back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, panel);

		// generate button generates excel file
		generateExcel = new CustomButton("Generate Excel", maxX / 2 - Utils.scale(430),
				(int) (maxY * 0.83) - Utils.scale(80) / 2, Utils.scale(330), Utils.scale(80), Utils.colours[2]);
		generateExcel.draw(g, panel);

		generateHTML = new CustomButton("Generate Report", maxX / 2 + Utils.scale(100),
				(int) (maxY * 0.83) - Utils.scale(80) / 2, Utils.scale(350), Utils.scale(80), Utils.colours[2]);
		generateHTML.draw(g, panel);

		// display error message using custom button as a label (unclickable)
		CustomButton errorButton = new CustomButton(errorMessage, maxX / 2, (int) (maxY * 0.88), 0, Utils.scale(30));
		errorButton.setSelectable(false);
		errorButton.draw(g, panel);

		repaint();
	}

	/**
	 * Filters the reasons and calls on the calls on the database to create excel
	 * file
	 * 
	 * @param type whether the output file is a HTML or Excel (0=Excel, 1=HTML)
	 */
	private void filterAndSubmit(int type) {

		SinglyLinkedList<String> reasons = reasonSelect.getSelectedTexts(); // creating arraylists of the selected
																			// factors
		SinglyLinkedList<String> serts = sertSelect.getSelectedTexts();
		SinglyLinkedList<String> coursesMissing = courseMissingSelect.getSelectedTexts();

		int grade = -1;
		try {
			grade = Integer.parseInt(gradeSelect.getSelectedText());
		} catch (NumberFormatException e){
		}

		String id = idField.getText(); // retrieving text from the text fields
		String earliestDate = earliestDateField.getText();
		String latestDate = latestDateField.getText();
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String maxTime = maxTimeField.getText();
		String minTime = minTimeField.getText();

		Timestamp earliestDateAsTimestamp; // creating time stamp objects to pass into database method
		Timestamp latestDateAsTimestamp;
		int idAsInt;
		int minTimeAsInt;
		int maxTimeAsInt;

		if (!Utils.isStudentId(id)) {
			errorMessage = "That is not a valid student id.";
		} else if (!Utils.isDate(earliestDate) && !Utils.isDate(latestDate)) {
			errorMessage = "That is not a valid date format.";
		} else if (!Utils.isTime(minTime) && !Utils.isTime(maxTime)) {
			errorMessage = "That is not a valid time format.";
		} else {
			errorMessage = "";
			earliestDateAsTimestamp = Utils.getTimeStamp(earliestDate);
			latestDateAsTimestamp = Utils.getTimeStamp(latestDate);
			idAsInt = Utils.getInt(id);
			minTimeAsInt = Utils.getInt(minTime);
			maxTimeAsInt = Utils.getInt(maxTime);

			// PASS IN grades
			if (type == 0) {
				try {
					signInManager.generateExcel(idAsInt, firstName, lastName, grade, earliestDateAsTimestamp, latestDateAsTimestamp, minTimeAsInt,
							maxTimeAsInt, reasons, serts, coursesMissing);
					resetFields();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (type == 1) {
				try {
					signInManager.generateHTML(idAsInt, firstName, lastName, grade, earliestDateAsTimestamp, latestDateAsTimestamp, minTimeAsInt,
							maxTimeAsInt, reasons, serts, coursesMissing);
					resetFields();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void resetFields() {
		reasonSelect.reset();
		sertSelect.reset();
		courseMissingSelect.reset();
		gradeSelect.reset();

		idField.reset();
		earliestDateField.reset();
		latestDateField.reset();
		firstNameField.reset();
		lastNameField.reset();
		maxTimeField.reset();
		minTimeField.reset();
	}

	/**
	 * The method to reinitialize validation variables when someone leaves the
	 * screen Reinitializes textfields as well
	 * 
	 * @param state indicates which state to switch to
	 */
	private void leaveScreen(int state) {
		errorMessage = "";
		display.changeState(state);
	}

	public class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		/**
		 * Checks which button was clicked and acts accordingly
		 * 
		 * @param e mouse event which occurred
		 */
		public void mouseClicked(MouseEvent e) {
			if (back.isMouseOnButton(panel)) { // back button returns to teacher dashboard
				leaveScreen(5);
				resetFields();
			} else if (generateExcel.isMouseOnButton(panel)) { // generate button creates excel
				filterAndSubmit(0);
			} else if (generateHTML.isMouseOnButton(panel)) {
				filterAndSubmit(1);
			}
		}

		public void mousePressed(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mouseReleased(MouseEvent e) {

		}
	}
}
