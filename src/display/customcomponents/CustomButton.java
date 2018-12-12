/**
 * [CustomButton.java]
 * the custom class button that can be selected
 * BEST OF ALL IT LOOKS SO MUCH BETTER THAN JAVA BUTTONS
 * December 2 2018
 */

package display.customcomponents;

import utilities.Utils;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.MouseInfo;

public class CustomButton {
	
	// Important class variables
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected String text;
    private Font buttonFont;
    private Color primaryBackgroundColour;
    private Color secondaryBackgroundColour = Utils.colours[0];
    private Color primaryTextColour = Utils.colours[0];
    private Color secondaryTextColour;
    private boolean selectable = true;
    private boolean appearSelected = false;
    
    /**
     * Constructor
     * @param text String text on the button
     * @param x button position x
     * @param y button position y
     * @param width button width
     * @param height button height
     * @param mainColor background colour of the button
     */
    public CustomButton(String text, int x, int y, int width, int height, Color mainColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.primaryBackgroundColour = mainColor;
        this.secondaryTextColour = mainColor;
        this.buttonFont = Utils.getFont("assets/Kollektif.ttf", Math.round(this.height *0.5));
        this.selectable = true;
        this.appearSelected = false;

    }
      
    /**
     * Constructor
     * @param text String text on the button
     * @param x button position x
     * @param y button position y
     * @param width button width
     * @param height button height
     */
    public CustomButton(String text, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.primaryBackgroundColour = Utils.colours[1];
        this.secondaryTextColour = Utils.colours[1];
        this.buttonFont = Utils.getFont("assets/Kollektif.ttf", Math.round(this.height * 0.8));
    }
    
    /**
     * isMouseOnButton
     * Checks if the mouse is over the button
     * @param panel that the button is on
     * @return true if on and false if not
     * @throws IllegalComponentStateException
     */
    public boolean isMouseOnButton(JPanel panel) throws IllegalComponentStateException{
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = panel.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

        return ((x >= this.x) && (x <= this.x + this.width) && (y >= this.y) && (y <= this.y + this.height));
    }

    /**
     * draw
     * draws the button
     * @param g
     * @param panel
     */
    public void draw(Graphics g, JPanel panel) {
        g.setColor(primaryTextColour);
        
        // Set colour based on whether it should be selected or not
        if ((isMouseOnButton(panel) && selectable) || appearSelected) {
            g.setColor(secondaryBackgroundColour);
        } else {
            g.setColor(primaryBackgroundColour);
        }
        
        // Fill button rectangle
        if (primaryBackgroundColour != null) {
            g.fillRect(x,y, width, height);
        }

        // Set font and text variables
        g.setFont(buttonFont);
        FontMetrics buttonFontMetrics = g.getFontMetrics(buttonFont);
        int textHeight = buttonFontMetrics.getMaxAscent();
        int textWidth = 0;
        try {
	        textWidth = buttonFontMetrics.stringWidth(text);
        } catch (NullPointerException e) {

        }

        // Set text colour and draw it
        if ((isMouseOnButton(panel) && selectable) || appearSelected) {
            g.setColor(secondaryTextColour);
        } else {
            g.setColor(primaryTextColour);
        }
        g.drawString(text, x + width /2 - textWidth/2, y + height/2 + textHeight/4);

    }

    /**
     * setSelectable
     * set whether the user can select the button
     * @param selectable whether the user can select the button
     */
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
		
	}
	
	/**
	 * changeSelectedAppearance
	 * makes the selected appearance the opposite of what it currently is
	 */
	public void changeSelectedAppearance() {
		if (appearSelected) {
			appearSelected = false;
		} else {
			appearSelected = true;
		}
	}

	/**
	 * setSelected
	 * Set whether the button is really selected
	 * @param selected true if selected and false otherwise
	 */
	public void setSelected(boolean selected) {
		appearSelected = selected;
	}

}
