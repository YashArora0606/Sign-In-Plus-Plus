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
    private JPanel panel;
    private final int maxX;
    private final int maxY;
    private CustomButton student;
    private CustomButton teacher;

    HomePanel(Window display) {
        this.panel = this;
        this.display = display;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.addMouseListener(new MyMouseListener());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Utils.colours[4]);
        g.fillRect(0, 0, maxX / 2, maxY);
        g.setColor(Utils.colours[1]);
        g.fillRect(maxX / 2, 0, maxX / 2, maxY);

        student = new CustomButton("Student",maxX/4 - Utils.scale(100),maxY/2 - Utils.scale(100),
                Utils.scale(200), Utils.scale(100), Utils.colours[2]);
        teacher = new CustomButton("Teacher",3*maxX/4 - Utils.scale(110),maxY/2 - Utils.scale(100),
                Utils.scale(220), Utils.scale(100), Utils.colours[3]);

        student.draw(g, panel);
        teacher.draw(g,panel);

        repaint();
    }


    private class MyMouseListener implements MouseListener{
        public void mouseEntered(MouseEvent e){
        }
        public void mouseClicked(MouseEvent e){
            if (student.isMouseOnButton(panel)){
                display.changeState(1);
            } else if (teacher.isMouseOnButton(panel)) {
                display.changeState(4);
            }
        }
        public void mousePressed(MouseEvent e){

        }
        public void mouseExited(MouseEvent e){

        }
        public void mouseReleased(MouseEvent e){

        }
    }


}
