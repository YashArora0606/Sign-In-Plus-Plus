package display;

import utilities.Utils;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.MouseInfo;

public class CustomButton {
    int x;
    int y;
    int width;
    int height;
    String text;
    Font buttonFont;

    Color primaryBackgroundColour;
    Color secondaryBackgroundColour = Utils.colours[0];
    Color primaryTextColour = Utils.colours[0];
    Color secondaryTextColour;

    public CustomButton(String text, int x, int y, int width, int height, Color mainColor) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.primaryBackgroundColour = mainColor;
        this.secondaryTextColour = mainColor;
        this.buttonFont = Utils.getFont("assets/Kollektif.ttf", Math.round(this.height *0.5));

    }
    
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

    public boolean isMouseOnButton(JPanel panel) {
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = panel.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX() - relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY() - relScreenLocation.getY());

        return ((x >= this.x) && (x <= this.x + this.width) && (y >= this.y) && (y <= this.y + this.height));
    }

    public void draw(Graphics g, JPanel panel) {
        if (isMouseOnButton(panel)) {
            g.setColor(secondaryBackgroundColour);
        } else {
            g.setColor(primaryBackgroundColour);
        }
        g.fillRect(x,y, width, height);

        g.setFont(buttonFont);
        FontMetrics buttonFontMetrics = g.getFontMetrics(buttonFont);
        int textWidth = buttonFontMetrics.stringWidth(text);
        int textHeight = buttonFontMetrics.getMaxAscent();

        if (isMouseOnButton(panel)) {
            g.setColor(secondaryTextColour);
        } else {
            g.setColor(primaryTextColour);
        }
        g.drawString(text, x + width /2 - textWidth/2, y + height/2 + textHeight/4);

    }
}
