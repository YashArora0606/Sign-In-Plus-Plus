package display.panels;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import exceptions.InvalidIdException;
import exceptions.NotSignedInException;
import utilities.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SignOutPanel extends JPanel {
	private JPanel panel;
	private Window display;
	private SignInManager signInManager;

	private JTextField idField;
	private CustomButton submit;
	private CustomButton back;

	private CustomButton studentId;
	
	private Dimension idSize;
	
	private String errorMessage = "";

	private int x;
	private int y;

	public SignOutPanel(Window display, SignInManager signInManager) throws IllegalComponentStateException {
		this.panel = this;
		this.display = display;
		this.signInManager = signInManager;
		this.addMouseListener(new MyMouseListener());
		this.setLayout(null);
		this.setBackground(Utils.colours[2]);

		this.x = display.maxX;
		this.y = display.maxY;

		idField = new JTextField(7);
		idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
		idField.setText("");
		idSize = idField.getPreferredSize();
		this.add(idField);
		this.addMouseListener(new MyMouseListener());

		idField.setBounds(display.maxX / 2 - idSize.width / 2, display.maxY / 2 - idSize.height - Utils.scale(100), idSize.width,
				idSize.height);

		idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
		idField.setBackground(null);

		setVisible(true);
	}

	/**
	 * paintComponent
	 * draws all buttons and text on screen
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, this);

		submit = new CustomButton("Submit", x / 2 - Utils.scale(250) / 2, (int) (y * 0.5), Utils.scale(250),
				Utils.scale(80), Utils.colours[1]);
		submit.draw(g, this);

		studentId = new CustomButton("Student Id", display.maxX / 2 - Utils.scale(220)/2,
				display.maxY / 2 - idSize.height - Utils.scale(165), Utils.scale(220), Utils.scale(50), Utils.colours[4]);
		studentId.setSelectable(false);
		studentId.draw(g, this);

		g.setColor(Utils.colours[0]);
		
		CustomButton errorButton = new CustomButton(errorMessage, display.maxX / 2 - Utils.scale(800)/2,
				display.maxY/30, Utils.scale(800), Utils.scale(50), null);
		errorButton.setSelectable(false);
		errorButton.draw(g, this);
		
		repaint();
	}

	/**
	 * submit
	 * sets all variables and re initializes placeholders in the button fields
	 * @return true = successfully submitted false = error thrown/user input incorrect
	 */
	private boolean submit() {
		String id = idField.getText();
		try {
			if (id.length() != 0) {
				signInManager.signOut(Integer.parseInt(id));
				errorMessage = "";
				idField.setText("");
			}
			return true;
		} catch (InvalidIdException | NotSignedInException e) {
			errorMessage = "Error: " + e.getMessage();
			return false;
		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		/**
		 * mouseClicked
		 * changes panel state, submits info, or does nothing. Based on where the user clicked
		 * @param e MouseEvent
		 */
		public void mouseClicked(MouseEvent e) {
			if (back.isMouseOnButton(panel)) {
				display.changeState(1);
			}

			if (submit.isMouseOnButton(panel)) {
				submit();
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
