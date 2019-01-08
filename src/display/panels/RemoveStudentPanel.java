/**
 * [RemoveStudentPanel.java]
 * The panel to be able to remove a student by the teacher from the dashboard
 * December 2 2018
 */

package display.panels;

import javax.swing.JPanel;
import javax.swing.JTextField;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import exceptions.StudentDoesNotExistException;
import utilities.SinglyLinkedList;
import utilities.Utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The panel to remove a student from the database and .csv file
 *
 * @author Katelyn Wang
 */
public class RemoveStudentPanel extends JPanel {
    private static final String STUDENT_FILE = "Student List/RHHSStudentList.csv";
    private final int maxX;
    private final int maxY;

    private JPanel panel;
    private Window display;
    private SignInManager signInManager;

    private JTextField idField;
    private CustomButton submit;
    private CustomButton back;

    private int attemptValidation = 0;
    private Dimension idSize;

    /**
     * Constructor
     *
     * @param display       the Window object to which this panel belongs
     * @param signInManager the manager to handle removing the student from the database
     */
    public RemoveStudentPanel(Window display, SignInManager signInManager) {
        this.panel = this;
        this.display = display;
        this.signInManager = signInManager;
        this.maxX = display.maxX;
        this.maxY = display.maxY;

        this.addMouseListener(new MyMouseListener());
        this.setLayout(null); //null layout to allow for absolute positioning
        this.setBackground(Utils.colours[2]);

        idField = new JTextField(7); //creating a text field and initializing length
        idField.setFont(Utils.getFont("assets/Kollektif.ttf", 50f)); //retrieving the font
        idSize = idField.getPreferredSize();
        this.add(idField);
        //absolute positioning to center the id field
        idField.setBounds(display.maxX / 2 - idSize.width / 2, display.maxY / 2 - idSize.height - Utils.scale(100), idSize.width,
                idSize.height);
        //creating the dashed border look for the text field
        idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));
        idField.setBackground(null);

        this.addMouseListener(new MyMouseListener());

        setVisible(true);
    }

    /**
     * The method to paint all components on the screen
     *
     * @param g The Graphics object with which
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, this);

        submit = new CustomButton("Submit", maxX / 2 - Utils.scale(250) / 2, (int) Math.round(0.45 * maxY), Utils.scale(250),
                Utils.scale(80), Utils.colours[1]);
        submit.draw(g, this);

        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);

        g.setColor(Utils.colours[0]);
        CustomButton studentId;
        studentId = new CustomButton("Student Id", display.maxX / 2 - Utils.scale(220) / 2,
                display.maxY / 2 - idSize.height - Utils.scale(165), Utils.scale(220), Utils.scale(50), Utils.colours[4]);
        studentId.setSelectable(false);
        studentId.draw(g, this);

        g.setFont(errorFont);
        if (attemptValidation == 1) {
            g.drawString("That is not an existing student number. Please try again.",
                    maxX / 2 - errorFontMetrics.stringWidth("That is not an existing student Number. Please try again.") / 2,
                    3 * maxY / 5);
        } else if (attemptValidation == 2) {
            g.drawString("Successfully removed student",
                    maxX / 2 - errorFontMetrics.stringWidth("Successfully removed student") / 2,
                    3 * maxY / 5);
        }

        repaint();
    }

    /**
     * The method for removing a student from the database - calls on signin manager method
     * Clears student from the student database, and removes all of their sessions
     * If the id is invalid or does not exist, returns false
     *
     * @return true if successful
     * @throws StudentDoesNotExistException If the student does not exist, throws the exception
     */
    private boolean removeStudent() throws StudentDoesNotExistException {
        if (idField.getText().length() != 9) {
            return false;
        }
        return signInManager.removeStudent(Integer.parseInt(idField.getText()));
    }

    /**
     * Reconciles the data against the student csv provided by user
     * Calls on method to read the current file
     * Calls on method to write to the file again, filtering out the removed student
     */
    private void reconcileFile() {
        SinglyLinkedList<String> studentText = readFile();
        writeFile(studentText);
    }

    /**
     * Reads the current file and saves all the lines to be filtered and rewritten
     *
     * @return A SinglyLinkedList of each line of the current csv file
     */
    private SinglyLinkedList<String> readFile() {
        SinglyLinkedList<String> file = new SinglyLinkedList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(STUDENT_FILE)));

            String line = reader.readLine(); //reading each line and checking that it has content
            while (line != null) {
                file.add(line); //if it has content, adding it to the list of text
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Using the SinglyLinkedList generated by reading the file originally, writes it back to the csv file,
     * as long as it doesn't contain the id of the student who was just removed
     *
     * @param text The SinglyLinkedList of each line of text from the original csv file
     */
    private void writeFile(SinglyLinkedList<String> text) {
        try {
            PrintWriter out = new PrintWriter(new File(STUDENT_FILE));
            String id = idField.getText(); //gets the id of the student who is being removed
            if (id.substring(0, 1).equals("0")) { //if the id entered begins with a 0, shortens it
                id = id.substring(1);
            }

            for (String line : text) {
                //iterates through each line of text, and writes it to the csv
                //as long as it doesn't contain the user who was just removed
                if (!line.contains(id)) {
                    out.println(line);
                }
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The method called when the user exits the page to go to a different panel
     *
     * @param state the identifier for the new panel to be displayed
     */
    private void leave(int state) {
        attemptValidation = 0; //resetting components on this panel
        idField.setText("");
        display.changeState(state); //displaying the new panel
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * Checking which buttons are clicked if a mouse click is detected
         *
         * @param e The mouse event which listens for a click
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) { //if the user clicks the back button, calls on method to reset components
                leave(5); //returns to the teacher dashboard panel
            }
            if (submit.isMouseOnButton(panel)) { //if the user clicks the submit button, tries to remove the student
                try {
                    if (removeStudent()) { //if it is successful, indicates it with the validation state
                        attemptValidation = 2;
                        reconcileFile(); //reconciles the update with the external csv file
                        idField.setText(""); //only clears text if the submission was successful
                    } else {
                        attemptValidation = 1; //if fails, indicates it with the validation state
                    }
                } catch (StudentDoesNotExistException error) { //if the student does not exist, indicates it with the validation state
                    attemptValidation = 1;
                    error.printStackTrace();
                }
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
