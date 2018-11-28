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

	private CustomButton submit;
	private CustomButton back;

	private int x;
	private int y;
	
	DropDownMenu reasonDropDown;
	DropDownMenu subjectDropDown;
	DropDownMenu courseMissingDropDown;
	
	private JPanel panel;

	SignInPanel(Window display, SignInManager signInManager) {

		this.panel = this;
		this.display = display;
		x = display.maxX;
		y = display.maxY;
	
		this.signInManager = signInManager;

		setBackground(Utils.colours[2]);
		setLayout(new BorderLayout());

		reasonDropDown = new DropDownMenu(SignInManager.getReasons(), "Reason");
		subjectDropDown = new DropDownMenu(SignInManager.getCourses(), "Subject");
		courseMissingDropDown = new DropDownMenu(SignInManager.getCourses(), "Course Missed");

		idField = new JTextField(7);
		idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
		idField.setText("");
		Dimension idSize = idField.getPreferredSize();
		idField.setBounds(display.maxX / 2 - idSize.width / 2, display.maxY / 2 - idSize.height, idSize.width,
				idSize.height);
		this.addMouseListener(new MyMouseListener());
		idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
		idField.setBackground(null);
		
		JPanel northPanel = new JPanel();
		
		northPanel.setBackground(null);
		northPanel.setPreferredSize(new Dimension(900, 100));
		northPanel.add(idField);
		northPanel.setOpaque(false);


		JPanel centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(display.maxX, 620));
		centerPanel.setBackground(null);
		//centerPanel.setLayout(new BorderLayout());
		centerPanel.add(reasonDropDown);
		centerPanel.add(subjectDropDown);
		centerPanel.add(courseMissingDropDown);
		centerPanel.setOpaque(false);

		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(display.maxX, 100));
		topPanel.setBackground(null);
		topPanel.setOpaque(false);
		
		add(topPanel, BorderLayout.NORTH);
		add(northPanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private boolean submit() {
		String id = idField.getText();
		String subject = subjectDropDown.getSelectedText();
		String reason = reasonDropDown.getSelectedText();
		String courseMissed = courseMissingDropDown.getSelectedText();
		
		System.out.println(id);
		System.out.println(subject);
		System.out.println(reason);
		System.out.println(courseMissed);

		return true;

//		try {
//			signInManager.signIn(id, subject, reason, courseMissed);
//			idField.setText("");
//			return true;
//		} catch (InvalidIdException | AlreadyLoggedInException e) {
//			e.printStackTrace();
//			return false;
//		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if(submit.isMouseOnButton(panel)) {
				submit();
			}
			
			if(back.isMouseOnButton(panel)) {
                display.changeState(1);
			}
			
			if(reasonDropDown.isMouseOnPanel(panel)) {
				reasonDropDown.drop();
			}
			if(subjectDropDown.isMouseOnPanel(panel)) {
				subjectDropDown.drop();
			}
			if(courseMissingDropDown.isMouseOnPanel(panel)) {
				courseMissingDropDown.drop();
			}
			
			
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

        back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, this);

		submit = new CustomButton("Submit", x/2 - Utils.scale(250)/2, (int)(y * 0.8), Utils.scale(250), Utils.scale(80), Utils.colours[1]);
		submit.draw(g, this);

		repaint();
	}
}
