package display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import datamanagement.SignInManager;
import utilities.Utils;

public class AddAndRemoveMenu extends JPanel {

	String title;

	ArrayList<String> items;

	ArrayList<CustomButton> itemButtons = new ArrayList<CustomButton>();
	ArrayList<CustomButton> xButtons = new ArrayList<CustomButton>();

	CustomTextField addItem;

	CustomButton submitButton;
	JLayeredPane pane;
	
	String message = "";

	AddAndRemoveMenu(ArrayList<String> items, String title, int maxY) {
		addItem = new CustomTextField("Add " + title);
		this.items = items;
		this.title = title;
		this.setPreferredSize(new Dimension(Utils.scale(300) + Utils.scale(400), maxY));
		this.addMouseListener(new MyMouseListener());
		this.setOpaque(false);
		
		setVisible(true);

		pane = new JLayeredPane();
		pane.setPreferredSize(new Dimension(getPreferredSize()));

		initializeButtons();

		addItem.setBounds(Utils.scale(300) + Utils.scale(50) + Utils.scale(20)*2, 0, addItem.getPreferredSize().width,
				addItem.getPreferredSize().height);
		addItem.setOpaque(false);

		pane.add(addItem);

		add(pane);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		CustomButton titleButton = new CustomButton(title + " List", Utils.scale(50), 0, Utils.scale(300), Utils.scale(50),
				Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);

		for (int i = 0; i < items.size(); i++) {
			itemButtons.get(i).draw(g, this);
			xButtons.get(i).draw(g, this);
		}
		
		for (int i = 0; i < xButtons.size(); i++) {
			if (isMouseOnButton(xButtons.get(i))) {
				itemButtons.get(i).setSelected(true);
			} else {
				itemButtons.get(i).setSelected(false);
			}
		}

		submitButton = new CustomButton("Submit", addItem.getX() + addItem.getPreferredSize().width/2 - Utils.scale(300)/2,
				addItem.getPreferredSize().height + Utils.scale(20), Utils.scale(300), Utils.scale(50),
				Utils.colours[3]);
		submitButton.draw(g, this);

		repaint();
	}

	public boolean isMouseOnPanel(JPanel panel) throws IllegalComponentStateException {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		Point relScreenLocation = panel.getLocationOnScreen().getLocation();
		int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
		int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

		return ((x >= 0) && (x <= panel.getWidth()) && (y >= 0) && (y <= panel.getHeight()));
		// return ((x >= panel.getX()) && (x <= panel.getX() + panel.getWidth()) && (y
		// >= panel.getY()) && (y <= panel.getY() + panel.getHeight()));
	}

	/**
	 * isMouseOnButton checks whether or not the cursor is over the button
	 * 
	 * @param b custom button to check
	 * @return true = is on button, false = not on button
	 * @throws IllegalComponentStateException
	 */
	public boolean isMouseOnButton(CustomButton b) throws IllegalComponentStateException {
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
		Point relScreenLocation = this.getLocationOnScreen().getLocation();
		int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
		int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

		return ((x >= b.x) && (x <= b.x + b.width) && (y >= b.y) && (y <= b.y + b.height));
	}

	public void updateSpacing() {
		for (int i = 0; i < items.size(); i++) {
			itemButtons.get(i).y = Utils.scale(50) * (i + 1);
			xButtons.get(i).y = Utils.scale(50) * (i + 1);
		}
	}
	
	public void alphabetize() {
		Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
		itemButtons.clear();
		xButtons.clear();
		initializeButtons();
	}
	
	public void initializeButtons() {
		for (int i = 0; i < items.size(); i++) {
			CustomButton b = new CustomButton(items.get(i), Utils.scale(50), Utils.scale(50) * (i + 1),
					Utils.scale(300), Utils.scale(50), Utils.colours[4]);
			b.setSelectable(false);

			CustomButton x = new CustomButton("X", 0, Utils.scale(50) * (i + 1), Utils.scale(50), Utils.scale(50),
					Utils.colours[2]);

			itemButtons.add(b);
			xButtons.add(x);
		}
	}
	
	public String getMessage() {
		return message;
	}
	
	public void updateTxt() {
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter("SERTList.txt"));
	            for (int i = 0; i < items.size(); i++) {
	                out.write(items.get(i));
	                out.newLine();
	            }
	            out.close();
	        } catch (IOException e) {
	        	e.printStackTrace();
	        }
		SignInManager.updateSerts(items);
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		public void mouseClicked(MouseEvent e) {

			for (int i = 0; i < xButtons.size(); i++) {
				if (isMouseOnButton(xButtons.get(i))) {
					message = "\"" + items.get(i) + "\" was successfuly removed.";
					items.remove(i);
					itemButtons.remove(i);
					xButtons.remove(i);
				}
				updateSpacing();
				updateTxt();
			}

			if (isMouseOnButton(submitButton)) {
				
				if (addItem.getText().isEmpty()) {
					message = "Please enter a field to be added.";
				} else if (items.contains(addItem.getText())) {
					message = "\"" + addItem.getText() + "\" has already been added.";
				} else {
					items.add(addItem.getText());
					itemButtons.add(new CustomButton(addItem.getText(), Utils.scale(50), 0,
							Utils.scale(300), Utils.scale(50), Utils.colours[3]));
					xButtons.add(new CustomButton("X", 0, 0, Utils.scale(50), Utils.scale(50),
							Utils.colours[3]));
					
					alphabetize();
					updateSpacing();
					message = "\"" + addItem.getText() + "\" was successfuly added.";
				}
				updateTxt();
				

				
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
