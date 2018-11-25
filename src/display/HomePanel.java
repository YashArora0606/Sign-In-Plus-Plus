package display;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import utilities.Utils;

class HomePanel extends JPanel {
    private Window display;
    private boolean inWindow = false;
    private final int maxX;
    private final int maxY;
    private int highlight = -1;

    HomePanel(Window display) {
        this.display = display;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g){
        g.setColor(new Color(191, 191, 191));
        g.fillRect(0,0, maxX/2, maxY);
        g.setColor(new Color(131, 131, 131));
        g.fillRect(maxX/2, 0, maxX/2, maxY);

        Font mainFont = Utils.getFont("assets/Hind-Light.ttf", 50.0f);
        FontMetrics fontMetrics = g.getFontMetrics(mainFont);
        g.setFont(mainFont);

        if (highlight == 0){
           g.fillRect(maxX/4-fontMetrics.stringWidth("Student")/2-20, maxY/2-3*fontMetrics.getHeight()/4-10, fontMetrics.stringWidth("Student")+40, 3*fontMetrics.getHeight()/4+10);
        } else if (highlight == 1){
           g.setColor(new Color(191, 191, 191));
           g.fillRect(3*maxX/4-fontMetrics.stringWidth("Teacher")/2-20, maxY/2-3*fontMetrics.getHeight()/4-10, fontMetrics.stringWidth("Teacher")+40, 3*fontMetrics.getHeight()/4+10);
        }

        g.setColor(new Color(58, 118, 189));
        g.drawString("Student", maxX/4-fontMetrics.stringWidth("Student")/2, maxY/2-fontMetrics.getHeight()/4);
        g.setColor(new Color(79, 183, 189));
        g.drawString("Teacher", 3*maxX/4-fontMetrics.stringWidth("Teacher")/2, maxY/2-fontMetrics.getHeight()/4);

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point relScreenLocation = this.getLocationOnScreen().getLocation();
        int x = (int) Math.round(mouseLocation.getX()-relScreenLocation.getX());
        int y = (int) Math.round(mouseLocation.getY()-relScreenLocation.getY());
        if (inWindow) {
            if ((x < maxX / 2) && (y < maxY)) {
                highlight = 0;
            } else if ((x < maxX) && (y < maxY)) {
                highlight = 1;
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
        public void mouseClicked(MouseEvent e){
            if (e.getX() < maxX/2){
                display.changeState(1);
            } else {
                display.changeState(4);
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
