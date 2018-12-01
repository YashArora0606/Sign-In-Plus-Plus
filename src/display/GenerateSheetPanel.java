package display;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.Dimension;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import datamanagement.SignInManager;
import utilities.Utils;

public class GenerateSheetPanel extends JPanel {
	private Window display;
	private JPanel panel;

	private int maxX;
	private int maxY;

	private CustomButton back;
	private CustomButton generate;

	private SelectMultipleMenu reasonSelect;
	private SelectMultipleMenu sertSelect;
	private SelectMultipleMenu courseMissingSelect;

	private CustomTextField idField;
	private CustomTextField earliestDateField;
	private CustomTextField latestDateField;
	private CustomTextField firstNameField;
	private CustomTextField lastNameField;
	private CustomTextField maxTimeField;
	private CustomTextField minTimeField;

	private String errorMessage = "";
	private static final int PADDING_CONSTANT = Utils.scale(20);

	GenerateSheetPanel(Window display) {
		this.display = display;
		this.panel = this;
		this.maxX = display.maxX;
		this.maxY = display.maxY;

		this.setBackground(Utils.colours[4]);
		this.setLayout(new BorderLayout());

		JLayeredPane pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(maxX, maxY));

		reasonSelect = new SelectMultipleMenu(SignInManager.reasons, "Reason");
		sertSelect = new SelectMultipleMenu(SignInManager.serts, "SERT");
		courseMissingSelect = new SelectMultipleMenu(SignInManager.courses, "Course Missing");

		reasonSelect.setBounds(
				maxX / 2 - reasonSelect.getPreferredSize().width - sertSelect.getPreferredSize().width / 2
						- PADDING_CONSTANT,
				Utils.scale(300),
				reasonSelect.getPreferredSize().width, reasonSelect.getPreferredSize().height);

		sertSelect.setBounds(
				maxX / 2 - sertSelect.getPreferredSize().width / 2, Utils.scale(300),
				sertSelect.getPreferredSize().width, sertSelect.getPreferredSize().height);

		courseMissingSelect.setBounds(maxX / 2 + sertSelect.getPreferredSize().width / 2 + PADDING_CONSTANT,
				Utils.scale(300),
				courseMissingSelect.getPreferredSize().width, courseMissingSelect.getPreferredSize().height);

		pane.add(reasonSelect);
		pane.add(sertSelect);
		pane.add(courseMissingSelect);

		idField = new CustomTextField("Student Id");
		idField.setBounds(maxX / 2 - idField.getPreferredSize().width / 2, PADDING_CONSTANT,
				idField.getPreferredSize().width, idField.getPreferredSize().height);

		firstNameField = new CustomTextField("First Name");
		firstNameField.setBounds(maxX / 2 - firstNameField.getPreferredSize().width / 2,
				idField.getBounds().y + idField.getPreferredSize().height + PADDING_CONSTANT,
				firstNameField.getPreferredSize().width, firstNameField.getPreferredSize().height);

		lastNameField = new CustomTextField("Last Name");
		lastNameField.setBounds(maxX / 2 - lastNameField.getPreferredSize().width / 2,
				firstNameField.getBounds().y + firstNameField.getPreferredSize().height + PADDING_CONSTANT,
				lastNameField.getPreferredSize().width, lastNameField.getPreferredSize().height);

		earliestDateField = new CustomTextField("Earliest Date (DD/MM/YYYY)");
		earliestDateField.setBounds(
				(int) (maxX / 2 - firstNameField.getPreferredSize().width / 2
						- earliestDateField.getPreferredSize().getWidth() - PADDING_CONSTANT),
				idField.getBounds().y + idField.getPreferredSize().height + PADDING_CONSTANT,
				firstNameField.getPreferredSize().width, firstNameField.getPreferredSize().height);

		latestDateField = new CustomTextField("Latest Date (DD/MM/YYYY)");
		latestDateField.setBounds(
				(int) (maxX / 2 - firstNameField.getPreferredSize().width / 2
						- latestDateField.getPreferredSize().getWidth() - PADDING_CONSTANT),
				firstNameField.getBounds().y + firstNameField.getPreferredSize().height + PADDING_CONSTANT,
				lastNameField.getPreferredSize().width, lastNameField.getPreferredSize().height);

		minTimeField = new CustomTextField("Min Time (Minutes)");
		minTimeField.setBounds((int) (maxX / 2 + firstNameField.getPreferredSize().width / 2 + PADDING_CONSTANT),
				idField.getBounds().y + idField.getPreferredSize().height + PADDING_CONSTANT,
				firstNameField.getPreferredSize().width, firstNameField.getPreferredSize().height);

		maxTimeField = new CustomTextField("Max Time (Minutes)");
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, panel);

		generate = new CustomButton("Filter", maxX / 2 - Utils.scale(200) / 2, (int) (maxY * 0.83) - Utils.scale(80) / 2,
				Utils.scale(200), Utils.scale(80), Utils.colours[2]);
		generate.draw(g, panel);

		CustomButton errorButton = new CustomButton(errorMessage, maxX / 2, (int) (maxY * 0.88), 0, Utils.scale(30));
		errorButton.setSelectable(false);
		errorButton.draw(g, panel);

		repaint();
	}

	private void filterAndSubmit() {

		ArrayList<String> reasons = reasonSelect.getSelectedTexts();
		ArrayList<String> serts = sertSelect.getSelectedTexts();
		ArrayList<String> coursesMissing = courseMissingSelect.getSelectedTexts();

		String id = idField.getText();
		String earliestDate = earliestDateField.getText();
		String latestDate = latestDateField.getText();
		String firstName = firstNameField.getText();
		String lastName = lastNameField.getText();
		String maxTime = maxTimeField.getText();
		String minTime = minTimeField.getText();

		Timestamp earliestDateAsTimestamp;
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
			
			// PASS THIS DATA INTO ALSTON'S METHOD
		}

	}

	public class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		public void mouseClicked(MouseEvent e) {
			if (back.isMouseOnButton(panel)) {
				display.changeState(5);
			} else if (generate.isMouseOnButton(panel)) {
				filterAndSubmit();
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
