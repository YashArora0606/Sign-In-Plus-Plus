package display.customcomponents;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import display.panels.GenerateSheetPanel.MyMouseListener;
import utilities.SinglyLinkedList;
import utilities.Utils;

public class SelectMultipleMenu extends DropDownMenu {

	private SinglyLinkedList<String> selectedTexts = new SinglyLinkedList<>();
	private SinglyLinkedList<CustomButton> buttons = new SinglyLinkedList<>();

	public SelectMultipleMenu(String[] items, String title) {
		super(items, title);
		this.setPreferredSize(new Dimension(180, 30 * (items.length + 1)));
		this.addMouseListener(new MyMouseListener());
		this.setOpaque(false);

		for (int i = 1; i <= items.length; i++) {
			CustomButton b = new CustomButton(items[i - 1], 0, (i * 30), 180, 30);
			b.setSelectable(false);
			buttons.add(b);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);

		CustomButton titleButton = new CustomButton(title, 0, 0, 180, 30, Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);

		for (int i = 0; i < items.length; i++) {
			buttons.get(i).draw(g, this);
		}

		repaint();
	}

	/**
	 * getSelectedTexts returns button values selected
	 * 
	 * @return ArrayList of Strings that holds every selected values
	 */
	public SinglyLinkedList<String> getSelectedTexts() {
		return selectedTexts;
	}

	private class MyMouseListener implements MouseListener {
		public void mouseEntered(MouseEvent e) {

		}

		public void mouseClicked(MouseEvent e) {

			for (int i = 0; i < items.length; i++) {
				if (isMouseOnButton(buttons.get(i))) {
					buttons.get(i).changeSelectedAppearance();

					if (selectedTexts.indexOf(buttons.get(i).text) != -1) {
						selectedTexts.remove(buttons.get(i).text);
					} else {
						selectedTexts.add(buttons.get(i).text);
					}
				}
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

	@Override
	public void reset() {
		selectedTexts.clear();
		for (int i = 0; i < items.length; i++) {
			buttons.get(i).setSelected(false);
		}
	}

}
