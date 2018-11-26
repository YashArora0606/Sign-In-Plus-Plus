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
		this.setLayout(null);
		idField = new JTextField(10);
		idField.setFont(Utils.getFont("assets/Hind-Light.ttf", 50f));
		idField.setText("Student Number");
		Dimension size = idField.getPreferredSize();
		this.add(idField);
		idField.setBounds(display.maxX/2-size.width/2, display.maxY/2-size.height, size.width, size.height - 35);
		this.addMouseListener(new MyMouseListener());

        //idField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        //idField.setBackground(null);

        setVisible(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Font mainFont = Utils.getFont("assets/Hind-Light.ttf", 50.0f);

		back = new CustomButton("Back", 0, 0, 115, 80, new Color(171, 206, 102));
		drawCustomButton(back, g);
		
		//submit = new CustomButton("Submit", display.maxX/2 - Utils.scale(155)/2, Utils.scale(320), Utils.scale(155), Utils.scale(80), new Color(142, 241, 228));
		submit = new CustomButton("Submit", Utils.scale(display.maxX/2), Utils.scale(320), Utils.scale(200), Utils.scale(110), new Color(142, 241, 228));
		drawCustomButton(submit, g);
		
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

	private void drawCustomButton(CustomButton b, Graphics g) {

		if (isMouseOnButton(b)) {
			g.setColor(b.secondaryBackgroundColour);
		} else {
			g.setColor(b.primaryBackgroundColour);
		}

		Font buttonFont = Utils.getFont("assets/Hind-Light.ttf", 38.0f);
		FontMetrics buttonFontMetrics = g.getFontMetrics(buttonFont);
		//g.fillRect(b.x, b.y, b.w, b.h);
		g.fillRect(b.x, b.y, b.w, b.h);

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

		Color primaryBackgroundColour;
		Color secondaryBackgroundColour = new Color(90,90,90);
		Color primaryTextColour = new Color(232, 232, 232);
		Color secondaryTextColour;

		CustomButton(String text, int x, int y, int w, int h, Color mainColor) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.text = text;
			this.primaryBackgroundColour = mainColor;
			this.secondaryTextColour = mainColor;
		}
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {
		}

		public void mouseClicked(MouseEvent e) {
			if (isMouseOnButton(back)) {
				display.changeState(0);
			} else if (isMouseOnButton(submit)) {
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
