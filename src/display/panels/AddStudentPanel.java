package display.panels;

import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import datamanagement.SignInManager;
import display.Window;
import display.customcomponents.CustomButton;
import utilities.Utils;

/**
 * [AddStudentPanel.java]
 * the panel which allows the teacher to add a student to the database
 *
 * @author Katelyn Wang
 * last updated 2018/12/11
 */

public class AddStudentPanel extends JPanel {

    // Important variables for the display
    private Window display;
    private SignInManager signInManager;
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField gradeField;
    private JPanel panel;
    private final int maxX;
    private final int maxY;
    private CustomButton back;
    private CustomButton submit;
    private int attemptValidation = 0;

    /**
     * Constructor
     *
     * @param display      Window object of the display itself
     * @param signInManager SignInManager object that is used to sign students in
     */
    public AddStudentPanel(Window display, SignInManager signInManager) {
        this.display = display;
        this.signInManager = signInManager;
        this.panel = this;
        this.maxX = display.maxX;
        this.maxY = display.maxY;
        this.setLayout(null);
        this.setBackground(Utils.colours[1]);

        // Set font and size
        Font mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45.0));

        // Create the text fields
        idField = new JTextField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        gradeField = new JTextField(20);

        // Use the font created
        idField.setFont(mainFont);
        firstNameField.setFont(mainFont);
        lastNameField.setFont(mainFont);
        gradeField.setFont(mainFont);

        // Add the id field
        Dimension size = idField.getPreferredSize();
        this.add(idField);
        idField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 2 * maxY / 10, Utils.scale(size.width),
                Utils.scale(size.height));
        idField.setOpaque(false);
        idField.setBackground(null);
        idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));

        // Add the first name field
        size = firstNameField.getPreferredSize();
        this.add(firstNameField);
        firstNameField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 3 * maxY / 10, Utils.scale(size.width),
                Utils.scale(size.height));
        firstNameField.setOpaque(false);
        firstNameField.setBackground(null);
        firstNameField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));

        // Add the last name field
        size = lastNameField.getPreferredSize();
        this.add(lastNameField);
        lastNameField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 4 * maxY / 10, Utils.scale(size.width),
                Utils.scale(size.height));
        lastNameField.setOpaque(false);
        lastNameField.setBackground(null);
        lastNameField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));

        // Add the grade field
        size = gradeField.getPreferredSize();
        this.add(gradeField);
        gradeField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 5 * maxY / 10, Utils.scale(size.width),
                Utils.scale(size.height));
        gradeField.setOpaque(false);
        gradeField.setBackground(null);
        gradeField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));

        this.addMouseListener(new MyMouseListener());
    }

    /**
     * paintComponent
     * draws all buttons and text on screen
     *
     * @param g Graphics
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, panel);

        CustomButton idLabel = new CustomButton("Student Id", (int) idField.getBounds().getX(),
                (int) idField.getBounds().getY() - (int) (Utils.scale(50) * 1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        idLabel.setSelectable(false);
        idLabel.draw(g, panel);

        CustomButton firstNameLabel = new CustomButton("First Name", (int) firstNameField.getBounds().getX(),
                (int) firstNameField.getBounds().getY() - (int) (Utils.scale(50) * 1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        firstNameLabel.setSelectable(false);
        firstNameLabel.draw(g, panel);

        CustomButton lastNameLabel = new CustomButton("Last Name", (int) lastNameField.getBounds().getX(),
                (int) lastNameField.getBounds().getY() - (int) (Utils.scale(50) * 1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        lastNameLabel.setSelectable(false);
        lastNameLabel.draw(g, panel);

        CustomButton gradeLabel = new CustomButton("Grade", (int) gradeField.getBounds().getX(),
                (int) gradeField.getBounds().getY() - (int) (Utils.scale(50) * 1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        gradeLabel.setSelectable(false);
        gradeLabel.draw(g, panel);

        submit = new CustomButton("Submit", maxX / 2 - Utils.scale(100), 3 * maxY / 5, Utils.scale(200), Utils.scale(80), Utils.colours[2]);
        submit.draw(g, panel);


        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);
        g.setFont(errorFont);
        g.setColor(Utils.colours[0]);

        if (attemptValidation == 2) {
            g.drawString("One of the fields may have been inputted incorrectly. Please try again.",
                    maxX / 2 - errorFontMetrics.stringWidth("One of the fields may have been inputted incorrectly. Please try again.") / 2,
                    (int) Math.round(0.75 * maxY));
        } else if (attemptValidation == 1) {
            g.drawString("Successfully added student",
                    maxX / 2 - errorFontMetrics.stringWidth("Successfully added student") / 2,
                    (int) Math.round(0.75 * maxY));
        }

        repaint();
    }

    /**
     * leaveScreen
     * Performs an action that occurs every time the user leaves the screen
     * such as clearing all fields
     *
     * @param state that is the state of the display that is being changed to
     */
    public void leaveScreen(int state) {
        attemptValidation = 0;
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        gradeField.setText("");
        display.changeState(state);
    }

    /**
     * addStudent
     * Attempts to add a student to the database.
     *
     * @return boolean true if student was successfully added, false otherwise
     * @throws exceptions.StudentAlreadyExistsException
     */
    public boolean addStudent() throws exceptions.StudentAlreadyExistsException {
        int id = Integer.parseInt(idField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int grade = Integer.parseInt(gradeField.getText());
        if (idField.getText().length() != 9) {
            return false;
        }
        if ((firstName.contains(" ")) || (lastName.contains(" "))) {
            return false;
        }
        writeToCSV(id, firstName, lastName, grade);
        return (signInManager.addStudent(id, firstName, lastName, grade));
    }

    public void writeToCSV(int id, String firstName, String lastName, int grade) {
        try {
            File csv = new File("database/RHHSStudentList.csv");
            FileOutputStream outputStream = new FileOutputStream(csv);
            PrintWriter out = new PrintWriter(outputStream, true);
            out.print(id + "," + firstName + "," + lastName + ",316," + grade + ",HR,");
            out.close();
        } catch (IOException e) {
        }
    }

    private class MyMouseListener implements MouseListener {
        /**
         * mouseClicked
         * method called when mouse is clicked
         * changes the panel or tries to submit the data
         *
         * @param e MouseEvent object
         */
        public void mouseClicked(MouseEvent e) {
            if (back.isMouseOnButton(panel)) {
                leaveScreen(5);
            } else if (submit.isMouseOnButton(panel)) {
                try {
                    if (addStudent()) {
                        attemptValidation = 1;
                        idField.setText("");
                        firstNameField.setText("");
                        lastNameField.setText("");
                        gradeField.setText("");
                    } else {
                        attemptValidation = 2;
                    }
                } catch (exceptions.StudentAlreadyExistsException error) {
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

        public void mouseEntered(MouseEvent e) {
        }
    }
}
