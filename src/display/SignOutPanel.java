package display;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagment.SignInManager;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import utilities.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SignOutPanel extends JPanel {
	private JPanel panel;
	private Window display;
	private SignInManager signInManager;

	private JTextField idField;
	private CustomButton submit;
	private CustomButton back;

	SignOutPanel(Window display, SignInManager signInManager) {
		this.panel = this;
		this.display = display;
		this.signInManager = signInManager;
		this.addMouseListener(new MyMouseListener());
		this.setLayout(null);
		this.setBackground(Utils.colours[2]);

		idField = new JTextField(7);
		idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
		idField.setText("");
		Dimension idSize = idField.getPreferredSize();
		this.add(idField);
		this.addMouseListener(new MyMouseListener());

		//idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		idField.setBounds(display.maxX/2-idSize.width/2, display.maxY/2-idSize.height-50, idSize.width, idSize.height);

        idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
        idField.setBackground(null);

        setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, panel);

		submit = new CustomButton("Submit", Utils.scale(display.maxX/2)-Utils.scale(80), Utils.scale(320), Utils.scale(160), Utils.scale(90), Utils.colours[2]);
		submit.draw(g, panel);

		repaint();
	}

	private void submit() {
		String id = idField.getText();
		idField.setText("");
		try {
			signInManager.signOut(Integer.parseInt(id));
			idField.setText(null);

		} catch (InvalidIdException | NotLoggedInException e) {
			e.printStackTrace();
		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if (back.isMouseOnButton(panel)) {
				display.changeState(1);
			} else if (submit.isMouseOnButton(panel)) {
				idField.setText("");
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
