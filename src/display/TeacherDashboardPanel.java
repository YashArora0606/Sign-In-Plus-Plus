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

public class TeacherDashboardPanel extends JPanel{
    private Window display;
    private final int maxX;
    private final int maxY;
    private int buttonWidth;
    private int buttonHeight;
    private int buttonSpace;
    private boolean inWindow = false;
    private int highlight = -1;

    TeacherDashboardPanel(Window display){
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.buttonWidth = (int) Math.round(0.6*maxX);
        this.buttonHeight = (int) Math.round((0.8*maxY)/5);
        this.buttonSpace = (int) Math.round((0.3*maxY)/6);
        this.display = display;
        this.setBackground(new Color(191, 191, 191));
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (highlight == 0){
            g.setColor(new Color(90,90,90));
        } else {
            g.setColor(new Color(107, 213, 225));
        }
        g.fillRect(maxX/2-buttonWidth/2, buttonSpace, buttonWidth, buttonHeight);
        if (highlight == 1){
            g.setColor(new Color(90,90,90));
        } else {
            g.setColor(new Color(255, 217, 142));
        }
        g.fillRect(maxX/2-buttonWidth/2, buttonHeight+buttonSpace*2, buttonWidth, buttonHeight);
        if (highlight == 2){
            g.setColor(new Color(90,90,90));
        } else {
            g.setColor(new Color(255, 182, 119));
        }
        g.fillRect(maxX/2-buttonWidth/2, buttonHeight*2+buttonSpace*3, buttonWidth, buttonHeight);
        if (highlight == 3){
            g.setColor(new Color(90,90,90));
        } else {
            g.setColor(new Color(255, 131, 100));
        }
        g.fillRect(maxX/2-buttonWidth/4, buttonHeight*3+buttonSpace*4, buttonWidth/2, buttonHeight);
        Font mainFont = Utils.getFont("assets/Hind-Light.ttf", Math.round(buttonHeight*0.5));
        FontMetrics mainFontMetrics = g.getFontMetrics(mainFont);

        g.setFont(mainFont);
        g.setColor(new Color(244, 249, 247));
        g.drawString("Add Student", maxX/2-mainFontMetrics.stringWidth("Add Student")/2, buttonSpace+mainFontMetrics.getMaxAscent());
        g.drawString("Remove Student", maxX/2-mainFontMetrics.stringWidth("Remove Student")/2, buttonHeight+buttonSpace*2+mainFontMetrics.getMaxAscent());
        g.drawString("Change Password", maxX/2-mainFontMetrics.stringWidth("Change Password")/2, buttonHeight*2+buttonSpace*3+mainFontMetrics.getMaxAscent());
        g.drawString("Home", maxX/2-mainFontMetrics.stringWidth("Home")/2, buttonHeight*3+buttonSpace*4+mainFontMetrics.getMaxAscent());

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = this.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX()-relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY()-relScreenLocation.getY());
        if (inWindow) {
            if ((x > maxX/2-buttonWidth/2) && (x < maxX/2-buttonWidth/2+buttonWidth)
                    &&(y > buttonSpace) && (y < buttonSpace+buttonHeight)) {
                highlight = 0;
            } else if ((x > maxX/2-buttonWidth/2) && (x < maxX/2-buttonWidth/2+buttonWidth)
                    &&(y > buttonSpace*2+buttonHeight) && (y < buttonSpace*2+buttonHeight*2)){
                highlight = 1;
            } else if ((x > maxX/2-buttonWidth/2) && (x < maxX/2-buttonWidth/2+buttonWidth)
                    &&(y > buttonSpace*3+buttonHeight*2) && (y < buttonSpace*3+buttonHeight*3)) {
                highlight = 2;
            } else if ((x > maxX/2-buttonWidth/4) && (x < maxX/2-buttonWidth/4+buttonWidth/2)
                    &&(y > buttonSpace*4+buttonHeight*3) && (y < buttonSpace*4+buttonHeight*4)){
                highlight = 3;
            } else {
                highlight = -1;
            }
        } else {
            highlight = -1;
        }

        repaint();
    }

    private class MyMouseListener implements MouseListener{
        public void mouseEntered(MouseEvent e){
            inWindow = true;
        }
        public void mouseClicked(MouseEvent e) {
            if ((e.getX() > maxX/2-buttonWidth/2) && (e.getX() < maxX/2-buttonWidth/2+buttonWidth)
                    &&(e.getY() > buttonSpace) && (e.getY() < buttonSpace*2+buttonHeight)) {
            } else if ((e.getX() > maxX/2-buttonWidth/2) && (e.getX() < maxX/2-buttonWidth/2+buttonWidth)
                    &&(e.getY() > buttonSpace*2+buttonHeight) && (e.getY() < buttonSpace*3+buttonHeight*2)){
            } else if ((e.getX() > maxX/2-buttonWidth/2) && (e.getX() < maxX/2-buttonWidth/2+buttonWidth)
                    &&(e.getY() > buttonSpace*3+buttonHeight*2) && (e.getY() < buttonSpace*4+buttonHeight*3)) {
                display.changeState(6);
            } else if ((e.getX() > maxX/2-buttonWidth/4) && (e.getX() < maxX/2-buttonWidth/4+buttonWidth/2)
                    &&(e.getY() > buttonSpace*4+buttonHeight*3) && (e.getY() < buttonSpace*5+buttonHeight*4)){
                display.changeState(0);
            }
        }
        public void mousePressed(MouseEvent e){

        }
        public void mouseExited(MouseEvent e){
            inWindow = false;
        }
        public void mouseReleased(MouseEvent e){

        }
    }
}
