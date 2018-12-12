/**
 * [DropDownMenu.java] 
 * Custom menu that allows the user to select from a dropdown
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
import javax.swing.JPanel;
import utilities.Utils;

/**
 * Custom dropdown menu class that looks good
 * @author Yash Arora
 */
public class DropDownMenu extends JPanel {
	
	// Important class variables
	private  String selectedText = "Select";
	protected String title;
    protected String[] items;
	
    /**
     * Constructor
     * @param items Array of the items in the menu
     * @param title String name of the menu
     */
	public DropDownMenu(String[] items, String title) {
		this.items = items;
		this.title = title;
		this.setPreferredSize(new Dimension(Utils.scale(180), Utils.scale(30)*2));
        setVisible(true);
	}
	
	/**
	 * getSelectedText
	 * @return String of the text the user has selected
	 */
	public String getSelectedText() {
		return selectedText;
	}
	
    /**
     * paintComponent
     * draws all buttons and text on screen
     * @param g Graphics
     */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// Title button
		CustomButton titleButton = new CustomButton(title, 0, 0, Utils.scale(180), Utils.scale(30), Utils.colours[3]);
		titleButton.setSelectable(false);
		titleButton.draw(g, this);
		
		// Selected text button
		CustomButton select = new CustomButton(selectedText, 0, Utils.scale(30), Utils.scale(180), Utils.scale(30), Utils.colours[4]);
		select.setSelectable(false);
		if (isMouseOnPanel(this)) {
			setSize(Utils.scale(180), Utils.scale(30) * (items.length + 2));
		} else {
			setSize(this.preferredSize());
		}
		select.draw(g, this);
		
		// All item buttons
		for (int i = 1; i <= items.length; i++) {
			CustomButton b = new CustomButton(items[i-1], 0, (i * Utils.scale(30)) + Utils.scale(30), Utils.scale(180), Utils.scale(30));
			if (b.isMouseOnButton(this) && isMouseOnPanel(this)) {
				selectedText = items[i-1];
			}
			b.draw(g, this);
		}
				
		repaint();
	}

    /**
     * isMouseOnPanel
     * Checks if the mouse is over the menu
     * @param panel that the menu is on
     * @return true if on and false if not
     * @throws IllegalComponentStateException
     */
	public boolean isMouseOnPanel(JPanel panel) throws IllegalComponentStateException{
		Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = panel.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

        return ((x >= 0) && (x <= panel.getWidth()) && (y >= 0) && (y <= panel.getHeight()));
	}

	/**
	 * drop
	 * method that closes the menu
	 */
	public void drop() {
		setSize(this.preferredSize());
	}
	
	/**
	 * method that resets the menu to original state
	 */
	public void reset() {
		selectedText = "Select";
	}
	
	/**
	 * getTitle
	 * @return String title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * getItems
	 * @return Array of items in the list
	 */
	public String[] getItems(){
		return this.items;
	}
}

