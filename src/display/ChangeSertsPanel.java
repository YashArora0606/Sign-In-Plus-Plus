/**
 * [PasswordPanel.java]
 * the panel which prompts the user for the password to enter the teacher dashboard
 * checks the password with the password text file and then rewrites the password to the text file
 * December 2 2018
 */

package display;

import javax.swing.JTextField;

import datamanagement.SignInManager;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;

import utilities.Utils;

public class ChangeSertsPanel extends JPanel {
	private Window display;
	private JPanel panel;
	private int maxX;
	private int maxY;
	private CustomButton back;
	private CustomButton submit;
	AddAndRemoveMenu a;

	String message = "SERTS will be updated automatically when you close the program.";
	String menuMessage = "";

	JLayeredPane pane;

	/**
	 * Constructor
	 * 
	 * @param display the Window object to which this panel belongs
	 */
	ChangeSertsPanel(Window display) {
		this.display = display;
		this.panel = this;
		this.maxX = display.maxX;
		this.maxY = display.maxY;

		setBackground(Utils.colours[1]);

		this.addMouseListener(new MyMouseListener());

		pane = new JLayeredPane();

		a = new AddAndRemoveMenu(SignInManager.getSertList(), "SERT", maxY);
//        a.setBounds(a.getBounds().x, Utils.scale(100), a.getBounds().width, a.getBounds().height);

		JPanel emptyTop = new JPanel();
		emptyTop.setPreferredSize(new Dimension(maxX, Utils.scale(200)));
		emptyTop.setOpaque(false);
		add(emptyTop, BorderLayout.NORTH);
		add(a, BorderLayout.SOUTH);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// back button leads to the home panel
		back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
		back.draw(g, panel);

		CustomButton messageButton = new CustomButton(message, maxX / 2, Utils.scale(40), Utils.scale(0),
				Utils.scale(80), null);
		messageButton.draw(g, panel);

		menuMessage = a.getMessage();
		CustomButton menuMessageButton = new CustomButton(menuMessage, maxX / 2, Utils.scale(100), Utils.scale(0),
				Utils.scale(80), null);
		menuMessageButton.draw(g, panel);

		repaint();
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		/**
		 * Checks which button was clicked and responds accordingly
		 * 
		 * @param e the mouse event which occurred
		 */
		public void mouseClicked(MouseEvent e) {
			if (back.isMouseOnButton(panel)) {
				display.changeState(5);
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
