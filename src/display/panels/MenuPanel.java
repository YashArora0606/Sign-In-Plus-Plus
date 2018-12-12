/**
 * [MenuPanel.java]
 * Panel to display the option to signin and signout
 * December 2 2018
 */

package display.panels;

import display.Window;
import display.customcomponents.CustomButton;
import utilities.Utils;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The panel for students to select whether to sign in or sign out
 * @author Katelyn Wang
 */
public class MenuPanel extends JPanel {
    private JPanel panel;
    private Window display;
    private final int maxX;
    private final int maxY;
    private CustomButton back;
    private CustomButton signIn;
    private CustomButton signOut;

    /**
     * Constructor
     * @param display the Window object to which this panel belongs
     */
    public MenuPanel(Window display) {
        this.panel = this;
        this.display = display;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.addMouseListener(new MyMouseListener());
    }

    /**
     * The method which paints the buttons onto the screen
     * @param g The Graphics object which paints the visuals
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Utils.colours[1]); //the left side colour
        g.fillRect(0, 0, maxX / 2, maxY);
        g.setColor(Utils.colours[4]); //the right side colour
        g.fillRect(maxX / 2, 0, maxX / 2, maxY);

        //the signin and signout button, which will each direct the individual to the signin/signout pages
        signIn = new CustomButton("Sign In", (int) (maxX * 0.25) - Utils.scale(200) / 2,
                maxY / 2 - Utils.scale(200) / 2, Utils.scale(200), Utils.scale(100), Utils.colours[4]);
        signOut = new CustomButton("Sign Out", (int) (maxX * 0.75) - Utils.scale(220) / 2,
                maxY / 2 - Utils.scale(200) / 2, Utils.scale(220), Utils.scale(100), Utils.colours[1]);

        //back button will go back to the home panel
        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);

        signIn.draw(g, panel);
        signOut.draw(g, panel);
        back.draw(g, panel);

        repaint();
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * When the mouse is clicked, checks which button is pressed and changes the screen accordingly
         * @param e the mouse event which occurred
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                display.changeState(0); //returns back to the home screen
            } else if (signIn.isMouseOnButton(panel)) {
                display.changeState(2); //displays the sign in panel
            } else if (signOut.isMouseOnButton(panel)) {
                display.changeState(3); //displays the sign out panel
            }
        }

        public void mousePressed(MouseEvent e) {

        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {

        }
    }

}
