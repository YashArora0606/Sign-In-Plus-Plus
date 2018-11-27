package display;

import javax.swing.JButton; 
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

import datamanagment.Database;
import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import utilities.Utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

class SignInPanel extends JPanel {

	private Window display;
	private Database database;

	private JTextField idField;
	private JComboBox<String> reasonField;
	private JComboBox<String> subjectField;
	private JComboBox<String> courseMissedField;

	private CustomButton submit;
	private CustomButton back;

	private int maxX;
	private int maxY;

	SignInPanel(Window display, Database database) {
		
		this.display = display;
		this.maxX = display.maxX;
		this.maxY = display.maxY;

		this.database = database;
		
		
		setBackground(Utils.colours[2]);
		
		DropDownMenu reasonDropDown = new DropDownMenu(Database.getReasons(), "Reason");
		DropDownMenu subjectDropDown = new DropDownMenu(Database.getCourses(), "Subject");
		DropDownMenu courseMissingDropDown = new DropDownMenu(Database.getCourses(), "Course Missed");
			
		idField = new JTextField(7);
		idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
		idField.setText("");
		Dimension idSize = idField.getPreferredSize();
		idField.setBounds(display.maxX/2-idSize.width/2, display.maxY/2-idSize.height-50, idSize.width, idSize.height);
		this.addMouseListener(new MyMouseListener());
        idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
        idField.setBackground(null);
        
//		add(reasonDropDown, BorderLayout.WEST);
//		add(subjectDropDown, BorderLayout.CENTER);
//		add(courseMissingDropDown, BorderLayout.EAST);
        
		JPanel id = new JPanel();
		id.setBackground(null);
		id.add(idField);
		
		add(idField);
		
//		add(id, BorderLayout.PAGE_START);
//		
//		JPanel dropdowns = new JPanel();
//		
//		dropdowns.setBackground(null);
//		dropdowns.setPreferredSize(new Dimension(600, 50));
//		dropdowns.add(reasonDropDown, BorderLayout.WEST);
//		dropdowns.add(subjectDropDown, BorderLayout.CENTER);
//		dropdowns.add(courseMissingDropDown, BorderLayout.EAST);
//		
//		add(dropdowns, BorderLayout.CENTER);
		
		//c.gridx = 2;
		
		

		

//        
//        idField = new JTextField(10);
//        idField.setFont(idField.getFont().deriveFont(35f));
//        add(idField);
//
//
//        reasonField = new JComboBox<>();
//        reasonField.addItem("Select Reason");
//        for (String reason : database.getReasons()) {
//            reasonField.addItem(reason);
//        }
//        add(reasonField);
//
//
//        subjectField = new JComboBox<>();
//        subjectField.addItem("Select Subject");
//        for (String subject : database.getCourses()) {
//            subjectField.addItem(subject);
//        }
//        add(subjectField);
//
//        courseMissedField = new JComboBox<>();
//        courseMissedField.addItem("Select Missing Course");
//        for (String courseMissed : database.getCourses()) {
//            courseMissedField.addItem(courseMissed);
//        }
//        add(courseMissedField);
//
//
//        submitButton = new JButton("Sign In");
//        submitButton.addActionListener(e -> submit());
//        add(submitButton);
//
//
//        backButton = new JButton("Back");
//        backButton.addActionListener(e -> display.changeState(1));
//        add(backButton);

		setVisible(true);
	}

	private void submit() {
		String id = idField.getText();
		idField.setText("");
		String subject = (String) subjectField.getSelectedItem();
		String reason = (String) reasonField.getSelectedItem();
		String courseMissed = (String) courseMissedField.getSelectedItem();

		try {
			database.signIn(id, subject, reason, courseMissed);
			idField.setText(null);

		} catch (InvalidIdException | AlreadyLoggedInException e) {
			e.printStackTrace();
		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back", 20, 20, 115, 60, Utils.colours[1]);
		back.draw(g, this);
		
		submit = new CustomButton("Submit", 250, 200, 250, 80, Utils.colours[1]);
		submit.draw(g, this);
		
		repaint();
	}
}
