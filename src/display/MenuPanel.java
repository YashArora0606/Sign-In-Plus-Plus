package display;
import utilities.Utils; 

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class MenuPanel extends JPanel {
    private JPanel panel;
    private Window display;
    private boolean inWindow = false;
    public final int maxX;
    public final int maxY;
    private int highlight = -1;
    private static final int BACKBUTTON_WIDTH = 150;
    private static final int BACKBUTTON_HEIGHT = 80;
    private CustomButton back;
    private CustomButton signIn;
    private CustomButton signOut;

    MenuPanel(Window display) {
        this.panel = this;
        this.display = display;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Utils.colours[1]);
        g.fillRect(0, 0, maxX / 2, maxY);
        g.setColor(Utils.colours[4]);
        g.fillRect(maxX / 2, 0, maxX / 2, maxY);

        signIn = new CustomButton("Sign In",Utils.scale(maxX/4-100),Utils.scale(maxY/2-100),
                Utils.scale(200), Utils.scale(100), Utils.colours[4]);
        signOut = new CustomButton("Sign Out",Utils.scale(3*maxX/4-110),Utils.scale(maxY/2-100),
                Utils.scale(220), Utils.scale(100), Utils.colours[1]);
        back = new CustomButton("Back",0,0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);


        signIn.draw(g, panel);
        signOut.draw(g,panel);
        back.draw(g, panel);

        repaint();
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e){
            inWindow = true;
        }
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)){
                display.changeState(0);
            } else if (signIn.isMouseOnButton(panel)){
                display.changeState(2);
            } else if (signOut.isMouseOnButton(panel)){
                display.changeState(3);
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
