/**
 * [SelectMultipleMenu.java] 
 * Custom menu that allows the user to select from a list
 * Most importantly it is extremely good looking ;)
 * @author Yash Arora
 * December 2 2018
 */

package display.customcomponents;

import java.awt.Dimension; 
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import utilities.SinglyLinkedList;
import utilities.Utils;

/**
 * Like the DropDownMenu class but doesn't actually drop down
 * @author Yash Arora
 */
public class SelectMultipleMenu extends DropDownMenu {

	// Important class lists
	private SinglyLinkedList<String> selectedTexts = new SinglyLinkedList<>();
	private SinglyLinkedList<CustomButton> buttons = new SinglyLinkedList<>();

	/**
	 * Constructor
	 * @param items List of items in the menu
	 * @param title String name of the menu
	 */
	public SelectMultipleMenu(String[] items, String title) {
		super(items, title);
		this.setPreferredSize(new Dimension(180, 30 * (items.length + 1)));
		this.addMouseListener(new MyMouseListener());
		this.setOpaque(false);

		// Add buttons to list
		for (int i = 1; i <= items.length; i++) {
			CustomButton b = new CustomButton(items[i - 1], 0, (i * 30), 180, 30);
			b.setSelectable(false);
			buttons.add(b);
		}
	}

    /**
     * paintComponent
     * draws all buttons and text on screen
     * @param g Graphics
     */
	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);

		// Title button
		CustomButton titleButton = new CustomButton(title, 0, 0, 180, 30, Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);

		// Draw all buttons
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

        /**
         * mouseClicked
         * changes jpanel based on button clicked or performs an action based on button pressed
         * @param e MouseEvent
         */
		public void mouseClicked(MouseEvent e) {

			for (int i = 0; i < items.length; i++) {
				
				// Change selected appearance when button is pressed
				if (isMouseOnButton(buttons.get(i))) {
					buttons.get(i).changeSelectedAppearance();
					
					// Select or unselect buttons based on current state
					if (selectedTexts.indexOf(buttons.get(i).text) != -1) {
						selectedTexts.remove(buttons.get(i).text);
					} else {
						selectedTexts.add(buttons.get(i).text);
					}
				}
			}

		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}

	/**
	 * isMouseOnButton checks whether or not the cursor is over the button
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

	/**
	 * reset
	 * method that sets the menu to its original state
	 */
	@Override
	public void reset() {
		selectedTexts.clear();
		for (int i = 0; i < items.length; i++) {
			buttons.get(i).setSelected(false);
		}
	}

}
