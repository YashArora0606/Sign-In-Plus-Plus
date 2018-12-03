/**
 * [HomePanel.java]
 * The first panel the user sees, has two avenues, one for students, and the other for teachers
 * Leads to the signin panel for students
 * Leads to the teacher dashboard password prompt for teachers
 * December 2 2018
 */

package display;

import javax.swing.JPanel;
import java.awt.Graphics;
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

    /**
     * Constructor
     * @param display the Window it belongs to (to change states)
     */
    HomePanel(Window display) {
        this.panel = this; //to reference this panel for relative mouse tracking
        this.display = display;
        this.maxX = display.maxX; //window dimensions
        this.maxY = display.maxY;
        this.addMouseListener(new MyMouseListener());
    }

    /**
     * paints the graphics of the panel
     * @param g the Graphics object to draw the visuals
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Utils.colours[4]); //the left half of the window
        g.fillRect(0, 0, maxX / 2, maxY);
        g.setColor(Utils.colours[1]); //the right half of the window
        g.fillRect(maxX / 2, 0, maxX / 2, maxY);

        //student button leads to signin page
        student = new CustomButton("Student",maxX/4 - Utils.scale(200)/2,maxY/2 - Utils.scale(200)/2,
                Utils.scale(200), Utils.scale(100), Utils.colours[2]);
        //teacher button leads to password prompt to access teacher dashboard
        teacher = new CustomButton("Teacher",3*maxX/4 - Utils.scale(200)/2,maxY/2 - Utils.scale(200)/2,
                Utils.scale(200), Utils.scale(100), Utils.colours[3]);

        student.draw(g, panel);
        teacher.draw(g,panel);

        repaint();
    }

    private class MyMouseListener implements MouseListener{
        public void mouseEntered(MouseEvent e){
        }

        /**
         * When the mouse is clicked, checks which button is being clicked, and then changes the state based on that
         * @param e the mouse event which occurred
         */
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
