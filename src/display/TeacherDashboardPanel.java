package display;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import utilities.Utils;

public class TeacherDashboardPanel extends JPanel {
	private JPanel panel;
	private Window display;
	private final int maxX;
	private final int maxY;
	private CustomButton addStudent;
	private CustomButton removeStudent;
	private CustomButton changePassword;
	private CustomButton generateSheet;
	private CustomButton back;
	private CustomButton changeSerts;

	TeacherDashboardPanel(Window display) {
		this.panel = this;
		this.maxX = display.maxX;
		this.maxY = display.maxY;
		this.display = display;
		this.addMouseListener(new MyMouseListener());

		setBackground(Utils.colours[4]);
	}

	/**
	 * paintComponent
	 * draws all buttons and text on screen
	 * @param g Graphics
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);

		int padding = Utils.scale(30);

		addStudent = new CustomButton("Add Student", maxX / 2 - Utils.scale(300) - padding, Utils.scale(400),
				Utils.scale(300), Utils.scale(80), Utils.colours[1]);
		removeStudent = new CustomButton("Remove Student", maxX / 2 + padding, Utils.scale(400), Utils.scale(350),
				Utils.scale(80), Utils.colours[1]);
		changePassword = new CustomButton("Change Password", maxX / 2 - Utils.scale(375) - padding, Utils.scale(520),
				Utils.scale(375), Utils.scale(80), Utils.colours[2]);
		generateSheet = new CustomButton("Generate Files", maxX / 2 + padding, Utils.scale(520), Utils.scale(300),
				Utils.scale(80), Utils.colours[2]);
		
		changeSerts = new CustomButton("Edit Serts", maxX / 2 - Utils.scale(400)/2, Utils.scale(640), Utils.scale(400),
				Utils.scale(80), Utils.colours[3]);
		// generateExcel.setSelectable(false);

		addStudent.draw(g, panel);
		removeStudent.draw(g, panel);
		changePassword.draw(g, panel);
		generateSheet.draw(g, panel);
		back.draw(g, panel);
		changeSerts.draw(g, panel);

		repaint();
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		/**
		 * mouseClicked
		 * changes jpanel based on button clicked
		 * @param e MouseEvent
		 */
		public void mouseClicked(MouseEvent e) {
			if (addStudent.isMouseOnButton(panel)) {
				display.changeState(7);
			} else if (removeStudent.isMouseOnButton(panel)) {
				display.changeState(8);
			} else if (changePassword.isMouseOnButton(panel)) {
				display.changeState(6);
			} else if (generateSheet.isMouseOnButton(panel)) {
				 display.changeState(9);
			} else if (back.isMouseOnButton(panel)) {
				display.changeState(0);
			} else if (changeSerts.isMouseOnButton(panel)) {
				display.changeState(10);
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
