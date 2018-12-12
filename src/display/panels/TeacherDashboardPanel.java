/**
 * [TeacherDashboardPanel.java]
 * the panel which allows the teacher to perform a variety of functions
 * December 2 2018
 */

package display.panels;

import javax.swing.JPanel;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import exceptions.ImproperFormatException;

import utilities.Utils;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

/**
 * The panel which displays the teacher dashboard
 * - contains functionality to add students, remove students, edit sert teachers, generate files and change password
 *
 * @author Katelyn Wang & Yash Arora
 * last update 2018/12/11
 */
public class TeacherDashboardPanel extends JPanel {
    private JPanel panel;
    private Window display;
    private final int maxX;
    private CustomButton addStudent;
    private CustomButton removeStudent;
    private CustomButton changePassword;
    private CustomButton generateSheet;
    private CustomButton back;
    private CustomButton changeSerts;
    private CustomButton configureStudents;
    private CustomButton messageButton;
    private String message = "";
    private SignInManager manager;

    /**
     * Constructor
     *
     * @param display the window on which this panel is displayed
     * @param manager the sign in manager to configure students from the csv file
     */
    public TeacherDashboardPanel(Window display, SignInManager manager) {
        this.panel = this;
        this.maxX = display.maxX;
        this.display = display;
        this.manager = manager;
        this.addMouseListener(new MyMouseListener());

        setBackground(Utils.colours[4]);
    }

    /**
     * paintComponent
     * draws all buttons and text on screen
     * @param g Graphics
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);

        int padding = Utils.scale(30);

        addStudent = new CustomButton("Add Student", maxX / 2 - Utils.scale(400) - padding, Utils.scale(400),
                Utils.scale(400), Utils.scale(80), Utils.colours[1]);
        removeStudent = new CustomButton("Remove Student", maxX / 2 + padding, Utils.scale(400), Utils.scale(400),
                Utils.scale(80), Utils.colours[1]);
        changePassword = new CustomButton("Change Password", maxX / 2 - Utils.scale(400) - padding, Utils.scale(520),
                Utils.scale(400), Utils.scale(80), Utils.colours[2]);
        generateSheet = new CustomButton("Generate Files", maxX / 2 + padding, Utils.scale(520), Utils.scale(400),
                Utils.scale(80), Utils.colours[2]);

        changeSerts = new CustomButton("Edit Serts", maxX / 2 - Utils.scale(400) - padding, Utils.scale(640), Utils.scale(400),
                Utils.scale(80), Utils.colours[3]);

        configureStudents = new CustomButton("Configure Students", maxX / 2 + padding, Utils.scale(640), Utils.scale(400),
                Utils.scale(80), Utils.colours[3]);

        messageButton = new CustomButton(message, maxX / 2, Utils.scale(280), Utils.scale(0),
                Utils.scale(80), Utils.colours[3]);
        messageButton.setSelectable(false);

        addStudent.draw(g, panel);
        removeStudent.draw(g, panel);
        changePassword.draw(g, panel);
        generateSheet.draw(g, panel);
        back.draw(g, panel);
        changeSerts.draw(g, panel);
        configureStudents.draw(g, panel);
        messageButton.draw(g, panel);

        repaint();
    }


    private class MyMouseListener implements MouseListener {

        /**
         * mouseClicked
         * changes jpanel based on button clicked or performs an action based on button pressed
         *
         * @param e MouseEvent
         */
        public void mouseClicked(MouseEvent e) {
            message = "";
            if (addStudent.isMouseOnButton(panel)) {
                display.changeState(7);
            } else if (removeStudent.isMouseOnButton(panel)) {
                display.changeState(8);
            } else if (changePassword.isMouseOnButton(panel)) {
                display.changeState(6);
            } else if (generateSheet.isMouseOnButton(panel)) {
                display.changeState(9);
            } else if (back.isMouseOnButton(panel)) {
                display.changeState(0);
            } else if (changeSerts.isMouseOnButton(panel)) {
                display.changeState(10);
            } else if (configureStudents.isMouseOnButton(panel)) {
                // Tries to configure students if possible
                try {
                    manager.configureStudents();
                    message = "Students have been configured.";
                } catch (IOException | ImproperFormatException e1) {
                    e1.printStackTrace();
                }
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

    }
}
