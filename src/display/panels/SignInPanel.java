package display.panels;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import display.customcomponents.DropDownMenu;
import exceptions.AlreadySignedInException;
import exceptions.InvalidIdException;
import utilities.Utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SignInPanel extends JPanel {

	private Window display;
	private SignInManager signInManager;

	private JTextField idField;

	private CustomButton submit;
	private CustomButton back;
	
	private CustomButton studentId;

	private int x;
	private int y;
	
	DropDownMenu reasonDropDown;
	DropDownMenu sertDropDown;
	DropDownMenu courseMissingDropDown;
	
	private Dimension idSize;
	
	private JPanel panel;
	
	private String errorMessage = "";

	public SignInPanel(Window display, SignInManager signInManager) throws IllegalComponentStateException{

		this.panel = this;
		this.display = display;
		x = display.maxX;
		y = display.maxY;
	
		this.signInManager = signInManager;
		this.addMouseListener(new MyMouseListener());


		setBackground(Utils.colours[2]);
		setLayout(new BorderLayout());

		reasonDropDown = new DropDownMenu(SignInManager.reasons, "Reason");
		sertDropDown = new DropDownMenu(SignInManager.serts, "SERT");
		courseMissingDropDown = new DropDownMenu(SignInManager.courses, "Course Missed");

		idField = new JTextField(7);
		idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
		idField.setText("");
		idSize = idField.getPreferredSize();
		idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
		idField.setBackground(null);


		JPanel northPanel = new JPanel();

		northPanel.setBackground(null);
		northPanel.setPreferredSize(new Dimension(Utils.scale(900), Utils.scale(400)));
		northPanel.setOpaque(false);
		northPanel.add(idField);


		JPanel centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(display.maxX, Utils.scale(800)));
		
		centerPanel.setBackground(null);
		//centerPanel.setLayout(new BorderLayout());
		centerPanel.add(reasonDropDown);
		centerPanel.add(sertDropDown);
		centerPanel.add(courseMissingDropDown);
		centerPanel.setOpaque(false);

		
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(display.maxX, Utils.scale(150)));
		topPanel.setBackground(null);
		topPanel.setOpaque(false);
		
		add(topPanel, BorderLayout.NORTH);
		add(northPanel, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	/**
	 * submit
	 * sets all variables and re initializes placeholders in the button fields
	 * @return true = successfully submitted false = error thrown/user input incorrect
	 */
	private boolean submit() {
		String id = idField.getText();
		String sert = sertDropDown.getSelectedText();
		String reason = reasonDropDown.getSelectedText();
		String courseMissed = courseMissingDropDown.getSelectedText();

		try {
			
			try {
				signInManager.signIn(Integer.parseInt(id), reason, sert, courseMissed);
				idField.setText("");
				errorMessage = "";
				
				sertDropDown.reset();
				reasonDropDown.reset();
				courseMissingDropDown.reset();

				
			} catch (NumberFormatException e) {
				throw new InvalidIdException(id);
			}
			


			return true;
		} catch (InvalidIdException | AlreadySignedInException e) {
			errorMessage = "Error: " + e.getMessage();
			return false;
		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if(submit.isMouseOnButton(panel)) {
				submit();
			}
			
			if(back.isMouseOnButton(panel)) {
				sertDropDown.reset();
				reasonDropDown.reset();
				courseMissingDropDown.reset();
                display.changeState(1);
			}
			
			if(reasonDropDown.isMouseOnPanel(panel)) {
				reasonDropDown.drop();
			}
			if(sertDropDown.isMouseOnPanel(panel)) {
				sertDropDown.drop();
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

	/**
	 * paintComponent
	 * draws all buttons and text on screen
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

        back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, this);

		submit = new CustomButton("Submit", x/2 - Utils.scale(250)/2, (int)(y * 0.8), Utils.scale(250), Utils.scale(80), Utils.colours[1]);
		submit.draw(g, this);
		
		studentId = new CustomButton("Student Id", display.maxX / 2 - Utils.scale(220)/2,
				display.maxY/15, Utils.scale(220), Utils.scale(50), Utils.colours[4]);
		studentId.setSelectable(false);
		studentId.draw(g, this);
		
		CustomButton errorButton = new CustomButton(errorMessage, display.maxX / 2 - Utils.scale(800)/2,
				display.maxY/30, Utils.scale(800), Utils.scale(50), null);
		errorButton.setSelectable(false);
		errorButton.draw(g, this);

		repaint();
	}
}
