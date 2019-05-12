/**
 * [SignOutPanel.java]
 * the panel which allows the student to sign out
 * December 2 2018
 */

package display.panels;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import exceptions.InvalidIdException;
import exceptions.NotSignedInException;
import utilities.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The panel which allows students to sign out
 * @author Yash Arora
 */
public class SignOutPanel extends JPanel {

    // Important variables to the display
    private JPanel panel;
    private Window display;
    private SignInManager signInManager;
    private JTextField idField;
    private CustomButton submit;
    private CustomButton back;
    private Dimension idSize;
    private String errorMessage = "";
    private int x;
    private int y;

    /**
     * Constructor
     * @param display Window object that is shown
     * @param signInManager that allows for signing students in
     * @throws IllegalComponentStateException
     */
    public SignOutPanel(Window display, SignInManager signInManager) throws IllegalComponentStateException {

        this.panel = this;
        this.display = display;
        this.signInManager = signInManager;
        this.addMouseListener(new MyMouseListener());
        this.setLayout(null);
        this.setBackground(Palette.colours[2]);
        this.x = display.displayWidth;
        this.y = display.displayHeight;
        this.addMouseListener(new MyMouseListener());

        // Create id field
        idField = new JTextField(7);
        idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f));
        idField.setText("");
        idSize = idField.getPreferredSize();
        this.add(idField);
        idField.setBounds(display.displayWidth / 2 - idSize.width / 2, display.displayHeight / 2 - idSize.height - Utils.scale(100), idSize.width,
                idSize.height);
        idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Palette.colours[0]));
        idField.setBackground(null);

        setVisible(true);
    }

    /**
     * paintComponent
     * draws all buttons and text on screen
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Back button
        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Palette.colours[3]);
        back.draw(g, this);

        // Submit button
        submit = new CustomButton("Submit", x / 2 - Utils.scale(250) / 2, (int) (y * 0.5), Utils.scale(250),
                Utils.scale(80), Palette.colours[1]);
        submit.draw(g, this);

        // Id label
        CustomButton studentId = new CustomButton("Student Id", display.displayWidth / 2 - Utils.scale(220) / 2,
                display.displayHeight / 2 - idSize.height - Utils.scale(165), Utils.scale(220), Utils.scale(50), Palette.colours[4]);
        studentId.setSelectable(false);
        studentId.draw(g, this);

        // Reset colour to text colour
        g.setColor(Palette.colours[0]);

        // Error label
        CustomButton errorButton = new CustomButton(errorMessage, display.displayWidth / 2 - Utils.scale(800) / 2,
                display.displayHeight / 30, Utils.scale(800), Utils.scale(50), null);
        errorButton.setSelectable(false);
        errorButton.draw(g, this);

        repaint();
    }

    /**
     * submit
     * sets all variables and re initializes placeholders in the button
     */
    private void submit() {
        String id = idField.getText();
        // Tries to sign student in
        try {
            if (id.length() != 0) {
                signInManager.signOut(Integer.parseInt(id));
                errorMessage = "";
                idField.setText("");
            }
        } catch (InvalidIdException | NotSignedInException e) {
            // Display error if exists
            errorMessage = "Error: " + e.getMessage();
        }
    }

    private class MyMouseListener implements MouseListener {

        /**
         * mouseClicked
         * changes panel state, submits info, or does nothing. Based on where the user clicked
         * @param e MouseEvent
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                errorMessage = "";
                idField.setText("");
                display.changeState(1);
            }

            if (submit.isMouseOnButton(panel)) {
                submit();
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
