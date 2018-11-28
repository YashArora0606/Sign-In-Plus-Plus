package display;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagment.SignInManager;
import exceptions.AlreadyLoggedInException;
import exceptions.InvalidIdException;
import utilities.Utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SignInPanel extends JPanel {

	private Window display;
	private SignInManager signInManager;

	private JTextField idField;
	private JComboBox<String> reasonField;
	private JComboBox<String> subjectField;
	private JComboBox<String> courseMissedField;

	private CustomButton submit;
	private CustomButton back;

	private int maxX;
	private int maxY;

	SignInPanel(Window display, SignInManager signInManager) {

		this.display = display;
		this.maxX = display.maxX;
		this.maxY = display.maxY;

		this.signInManager = signInManager;

		setBackground(Utils.colours[2]);
		setLayout(new BorderLayout());

		DropDownMenu reasonDropDown = new DropDownMenu(SignInManager.getReasons(), "Reason");
		DropDownMenu subjectDropDown = new DropDownMenu(SignInManager.getCourses(), "Subject");
		DropDownMenu courseMissingDropDown = new DropDownMenu(SignInManager.getCourses(), "Course Missed");

		idField = new JTextField(7);
		idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
		idField.setText("");
		Dimension idSize = idField.getPreferredSize();
		idField.setBounds(display.maxX / 2 - idSize.width / 2, display.maxY / 2 - idSize.height, idSize.width,
				idSize.height);
		this.addMouseListener(new MyMouseListener());
		idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
		idField.setBackground(null);

//        JPanel dropdowns = new JPanel();
//        dropdowns.add(reasonDropDown, BorderLayout.WEST);
//        dropdowns.add(subjectDropDown, BorderLayout.SOUTH);
//        dropdowns.add(courseMissingDropDown, BorderLayout.EAST);
		
		JPanel northPanel = new JPanel();
		//northPanel.setLayout(null);
		
		northPanel.setBackground(null);
		northPanel.setPreferredSize(new Dimension(900, 300));
		northPanel.add(idField);

		JPanel centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(700, 300));
		centerPanel.setBackground(null);
		//centerPanel.setLayout(new BorderLayout());
		centerPanel.add(reasonDropDown);
		centerPanel.add(subjectDropDown);
		centerPanel.add(courseMissingDropDown);
		
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);


		//centerpanel
		// add centerpanel to center
		// in centerpanel add dropdowns to areas
//
//		add(reasonDropDown, BorderLayout.WEST);
//		add(subjectDropDown, BorderLayout.SOUTH);
//		add(courseMissingDropDown, BorderLayout.EAST);

		setVisible(true);
	}

	private void submit() {
		String id = idField.getText();
		idField.setText("");
		String subject = (String) subjectField.getSelectedItem();
		String reason = (String) reasonField.getSelectedItem();
		String courseMissed = (String) courseMissedField.getSelectedItem();

		try {
			signInManager.signIn(id, subject, reason, courseMissed);
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

//	public void paintComponent(Graphics g) {
//		super.paintComponent(g);
//
//		back = new CustomButton("Back", 20, 20, 115, 60, Utils.colours[1]);
//		back.draw(g, this);
//
//		submit = new CustomButton("Submit", 250, 200, 250, 80, Utils.colours[1]);
//		submit.draw(g, this);
//
//		repaint();
//	}
}
