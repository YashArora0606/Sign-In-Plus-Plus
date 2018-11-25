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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class SignOutPanel extends JPanel {

	private Window display;
	private Database database;

	private JTextField idField;
	private CustomButton submit;
	private CustomButton back;
	
	private JTextArea idArea;

	SignOutPanel(Window display, Database database) {
		this.display = display;
		this.database = database;
		this.addMouseListener(new MyMouseListener());

        idField = new JTextField(10);

        //idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        //idField.setBackground(null);
        idField.setFont(Utils.getFont("assets/Hind-Light.ttf", 30f));
        idField.setText("Student Number");

        
        add(idField);

        setVisible(true);
	}

	public void paintComponent(Graphics g) {

		Font mainFont = Utils.getFont("assets/Hind-Light.ttf", 50.0f);
		FontMetrics mainFontMetrics = g.getFontMetrics(mainFont);

		back = new CustomButton("Back", 40, 40, 115, 80);
		drawCustomButton(back, g);
		
		submit = new CustomButton("Submit", display.maxX/2 - 155/2, display.maxY - 200, 155, 80);
		drawCustomButton(submit, g);
		
		repaint();
	}

	private void submit() {
		String id = idField.getText();

		try {
			database.signOut(id);
			idField.setText(null);

		} catch (InvalidIdException | NotLoggedInException e) {
			e.printStackTrace();
		}
	}

	private void drawCustomButton(CustomButton b, Graphics g) {

		if (isMouseOnButton(b)) {
			g.setColor(b.secondaryBackgroundColour);
		} else {
			g.setColor(b.primaryBackgroundColour);
		}

		Font buttonFont = Utils.getFont("assets/Hind-Light.ttf", 38.0f);
		FontMetrics buttonFontMetrics = g.getFontMetrics(buttonFont);
		//g.fillRect(b.x, b.y, b.w, b.h);
		g.fillRoundRect(b.x, b.y, b.w, b.h, b.h/2, b.h/2);

		g.setFont(buttonFont);

		int textWidth = buttonFontMetrics.stringWidth(b.text);
		int textHeight = buttonFontMetrics.getMaxAscent();

		if (isMouseOnButton(b)) {
			g.setColor(b.secondaryTextColour);
		} else {
			g.setColor(b.primaryTextColour);
		}
		

		g.drawString(b.text, b.x + 20, b.y + b.h - textHeight/2 - 3);
	}

	private boolean isMouseOnButton(CustomButton b) {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		Point relScreenLocation = this.getLocationOnScreen().getLocation();
		int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
		int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

		if (x >= b.x && x <= b.x + b.w && y >= b.y && y <= b.y + b.h) {
			return true;
		} else {
			return false;
		}
	}

	private class CustomButton {
		int x;
		int y;
		int w;
		int h;
		String text;

		Color primaryBackgroundColour = new Color(230, 230, 250);
		Color secondaryBackgroundColour = new Color(0, 0, 0);
		Color primaryTextColour = new Color(0, 0, 0);
		Color secondaryTextColour = new Color(230, 230, 250);

		CustomButton(String text, int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.text = text;
		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if (isMouseOnButton(back)) {
				display.changeState(0);
			} else if (isMouseOnButton(submit)) {
				// Clear text field
				// Submit the text in it
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
