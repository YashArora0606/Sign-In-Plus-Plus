/**
 * [ChangeSertsPanel.java]
 * the panel which prompts the user for the password to enter the teacher dashboard
 * checks the password with the password text file and then rewrites the password to the text file
 * December 2 2018
 */

package display.panels;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.AddAndRemoveMenu;
import display.customcomponents.CustomButton;
import utilities.Utils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The panel to edit the sert teachers (can add and remove them)
 * Will automatically sort alphabetically
 * Updates to the database when program is closed
 *
 * @author Yash Arora
 */
public class ChangeSertsPanel extends JPanel {
    private Window display;
    private JPanel panel;
    private final int maxX;
    private final int maxY;

    private CustomButton back;
    private AddAndRemoveMenu addAndRemoveMenu;

    private static final String MESSAGE = "SERTS will be updated automatically when you close the program.";
    private String menuMessage = "";

    /**
     * Constructor
     *
     * @param display the Window object to which this panel belongs
     */
    public ChangeSertsPanel(Window display, SignInManager manager) {
        this.display = display;
        this.panel = this;
        this.maxX = display.displayWidth;
        this.maxY = display.displayHeight;

        setBackground(Palette.colours[1]);

        this.addMouseListener(new MyMouseListener());

        addAndRemoveMenu = new AddAndRemoveMenu(manager, manager.getSertArrayList(), "SERT", maxY);

        JPanel emptyTop = new JPanel();
        emptyTop.setPreferredSize(new Dimension(maxX, Utils.scale(200)));
        emptyTop.setOpaque(false);
        add(emptyTop, BorderLayout.NORTH);
        add(addAndRemoveMenu, BorderLayout.SOUTH);

    }

    /**
     * Paints all the buttons and graphics onto the screen
     *
     * @param g The graphic object to draw all the visuals
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // back button leads to the home panel
        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Palette.colours[3]);
        back.draw(g, panel);

        CustomButton messageButton = new CustomButton(MESSAGE, maxX / 2, Utils.scale(40), Utils.scale(0),
                Utils.scale(80), null);
        messageButton.draw(g, panel);

        menuMessage = addAndRemoveMenu.getMessage();
        CustomButton menuMessageButton = new CustomButton(menuMessage, maxX / 2, Utils.scale(100), Utils.scale(0),
                Utils.scale(80), null);
        menuMessageButton.draw(g, panel);

        repaint();
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Checks which button was clicked and responds accordingly
         *
         * @param e the mouse event which occurred
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                display.changeState(5);
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
