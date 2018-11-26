package display;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import datamanagment.Database;
import exceptions.InvalidIdException;
import exceptions.NotLoggedInException;
import utilities.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SignOutPanel extends JPanel {
	private JPanel panel;
	private Window display;
	private Database database;

	private JTextField idField;
	private CustomButton submit;
	private CustomButton back;

	private JTextArea idArea;

	SignOutPanel(Window display, Database database) {
		this.panel = this;
		this.display = display;
		this.database = database;
		this.addMouseListener(new MyMouseListener());
		this.setLayout(null);
		idField = new JTextField(10);
		idField.setFont(Utils.getFont("assets/Hind-Light.ttf", 50f));
		idField.setText("Student Number");
		Dimension size = idField.getPreferredSize();
		this.add(idField);
		idField.setBounds(display.maxX/2-size.width/2, display.maxY/2-size.height, size.width, size.height);
		this.addMouseListener(new MyMouseListener());

        //idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        //idField.setBackground(null);

        setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back", 0, 0, 115, 80, Utils.colours[1]);
		back.draw(g, panel);
		
		submit = new CustomButton("Submit", Utils.scale(display.maxX/2) - Utils.scale(155)/2, Utils.scale(320), Utils.scale(155), Utils.scale(80), Utils.colours[2]);
		submit.draw(g, panel);
		
		repaint();
	}

	private void submit() {
		String id = idField.getText();
		idField.setText("");
		try {
			database.signOut(id);
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
				display.changeState(0);
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
