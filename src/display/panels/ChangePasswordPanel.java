/**
 * [ChangePasswordPanel.java]
 * the panel which allows the teacher to change their password
 * December 2 2018
 */

package display.panels;

import display.Window;
import display.customcomponents.CustomButton;
import utilities.Utils;

import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The panel to change the password to the teacher dashboard
 *
 * @author Katelyn Wang
 */
public class ChangePasswordPanel extends JPanel {
    JTextField passwordField;
    private JPanel panel;
    private final int maxX;
    private final int maxY;
    private Window display;
    private int attemptSuccess = 0;
    private Font mainFont;
    private CustomButton back;
    private CustomButton submit;

    /**
     * Constructor
     *
     * @param display Window on which this panel is displayed
     */
    public ChangePasswordPanel(Window display) {
        this.panel = this;
        this.maxX = display.displayWidth;
        this.maxY = display.displayHeight;
        this.display = display;
        this.setLayout(null); //for absolute positioning
        this.addMouseListener(new MyMouseListener());

        // Set font
        mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45.0));

        // Create password field
        passwordField = new JTextField(20);
        passwordField.setFont(mainFont);
        Dimension size = passwordField.getPreferredSize();
        this.add(passwordField);
        //setting bounds to assign location on panel
        passwordField.setBounds(maxX / 2 - Utils.scale(size.width / 2), maxY / 2 - 2 * Utils.scale(size.height),
                Utils.scale(size.width), Utils.scale(size.height));

        // Set background
        setBackground(Palette.colours[1]);
    }

    /**
     * paintComponent draws all buttons and text on screen
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Back button
        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Palette.colours[3]);
        back.draw(g, panel);

        // Submit button
        submit = new CustomButton("Submit", maxX / 2 - Utils.scale(100), maxY / 2, Utils.scale(200), Utils.scale(80),
                Palette.colours[2]);
        submit.draw(g, panel);

        // Set appropriate fonts
        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30)); //font for error message
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);

        g.setColor(Palette.colours[0]);

        // Add password label
        CustomButton passwordLabel = new CustomButton("Change password", display.displayWidth / 2 - Utils.scale(250) / 2,
                passwordField.getY() - (int) (Utils.scale(50) * (1.2)), Utils.scale(250), Utils.scale(50),
                Palette.colours[4]);
        passwordLabel.setSelectable(false);
        passwordLabel.draw(g, panel);

        g.setFont(errorFont);

        // Determine the message to be shown
        if (attemptSuccess == 1) {
            g.drawString("Invalid Password. Please enter a password of 8+ characters", maxX / 2
                            - errorFontMetrics.stringWidth("Invalid Password. Please enter a password of 8+ characters") / 2,
                    maxY / 4);
        } else if (attemptSuccess == 2) {
            g.drawString("Successfully changed password!",
                    maxX / 2 - errorFontMetrics.stringWidth("Successfully changed password!") / 2, maxY / 4);
        }

        repaint();
    }

    /**
     * Checks if the password is longer than or equal to 8 characters to make it valid
     *
     * @param string password to check
     * @return
     */
    private boolean isValidPassword(String string) {
        return (string.length() >= 8);
    }

    /**
     * Writes the password to a file
     *
     * @param string password to be written
     */
    private void writeToFile(String string) {
        try {
            File passwordFile = new File("assets/password.txt");
            PrintWriter out = new PrintWriter(passwordFile);
            out.println(string);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if it is valid
     * Encrypts the password
     * Calls on method to write the password to the file
     *
     * @return true if successful
     */
    private boolean submit() {
        String enteredPass = passwordField.getText();
        if (isValidPassword(enteredPass)) {
            writeToFile(Utils.encode(enteredPass));
            return true;
        }
        return false;
    }

    /**
     * Runs when the screen is left and clears fields
     *
     * @param state the identifier number to change the screen to the appropriate panel
     */
    public void leaveScreen(int state) {
        attemptSuccess = 0;
        passwordField.setText("");
        display.changeState(state);
    }

    private class MyMouseListener implements MouseListener {

        /**
         * runs if the mouse is clicked
         *
         * @param e MouseEvent
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                leaveScreen(5);
            } else if (submit.isMouseOnButton(panel)) {
                // Try to submit
                if (submit()) {
                    attemptSuccess = 2;
                    passwordField.setText("");
                } else {
                    attemptSuccess = 1;
                }
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
