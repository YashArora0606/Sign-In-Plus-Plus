package display;

import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.FontMetrics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import datamanagement.SignInManager;
import utilities.Utils;

public class AddStudentPanel extends JPanel {
    private Window display;
    private SignInManager signInManager;
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField gradeField;
    private JPanel panel;
    private int maxX;
    private int maxY;
    private CustomButton back;
    private CustomButton submit;
    private int attemptValidation = 0;

    AddStudentPanel(Window display, SignInManager signInManager) {
        this.display = display;
        this.signInManager = signInManager;
        this.panel = this;
        this.maxX = display.maxX;
        this.maxY = display.maxY;

        this.setLayout(null);
        this.setBackground(Utils.colours[1]);
        idField = new JTextField(20);
        firstNameField = new JTextField(20);
        lastNameField = new JTextField(20);
        gradeField = new JTextField(20);

        Font mainFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(45.0));
        idField.setFont(mainFont);
        firstNameField.setFont(mainFont);
        lastNameField.setFont(mainFont);
        gradeField.setFont(mainFont);

        Dimension size = idField.getPreferredSize();
        this.add(idField);
        idField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 2*maxY/10, Utils.scale(size.width), Utils.scale(size.height));
        idField.setOpaque(false);
        idField.setBackground(null);
		idField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));


        size = firstNameField.getPreferredSize();
        this.add(firstNameField);
        firstNameField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 3*maxY/10, Utils.scale(size.width), Utils.scale(size.height));
        firstNameField.setOpaque(false);
        firstNameField.setBackground(null);
		firstNameField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));


        size = lastNameField.getPreferredSize();
        this.add(lastNameField);
        lastNameField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 4*maxY/10, Utils.scale(size.width), Utils.scale(size.height));
        lastNameField.setOpaque(false);
        lastNameField.setBackground(null);
		lastNameField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));


        size = gradeField.getPreferredSize();
        this.add(gradeField);
        gradeField.setBounds(maxX / 2 - Utils.scale(size.width / 2), 5*maxY/10, Utils.scale(size.width), Utils.scale(size.height));
        gradeField.setOpaque(false);
        gradeField.setBackground(null);
		gradeField.setBorder(javax.swing.BorderFactory.createDashedBorder(Utils.colours[0]));

        this.addMouseListener(new MyMouseListener());
    }

//    public void initialize(){
////        idField.setText("ID Number");
////        firstNameField.setText("First Name");
////        lastNameField.setText("Last Name");
////        gradeField.setText("Grade");
//    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        back = new CustomButton("Back", 0, 0, Utils.scale(115), Utils.scale(80), Utils.colours[3]);
        back.draw(g, panel);

        CustomButton idLabel = new CustomButton("Student Id", (int)idField.getBounds().getX(),
        		(int)idField.getBounds().getY() - (int)(Utils.scale(50)*1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        idLabel.setSelectable(false);
        idLabel.draw(g, panel);
        
        CustomButton firstNameLabel = new CustomButton("First Name", (int)firstNameField.getBounds().getX(),
        		(int)firstNameField.getBounds().getY() - (int)(Utils.scale(50)*1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        firstNameLabel.setSelectable(false);
        firstNameLabel.draw(g, panel);
        
        CustomButton lastNameLabel = new CustomButton("Student Id", (int)lastNameField.getBounds().getX(),
        		(int)lastNameField.getBounds().getY() - (int)(Utils.scale(50)*1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        lastNameLabel.setSelectable(false);
        lastNameLabel.draw(g, panel);
        
        CustomButton gradeLabel = new CustomButton("First Name", (int)gradeField.getBounds().getX(),
        		(int)gradeField.getBounds().getY() - (int)(Utils.scale(50)*1.2), Utils.scale(200), Utils.scale(50), Utils.colours[4]);
        gradeLabel.setSelectable(false);
        gradeLabel.draw(g, panel);
        
        submit = new CustomButton("Submit", maxX / 2 - Utils.scale(100), 3*maxY/5, Utils.scale(200), Utils.scale(80), Utils.colours[2]);
        submit.draw(g, panel);
        
        

        Font errorFont = Utils.getFont("assets/Kollektif.ttf", Utils.scale(30));
        FontMetrics errorFontMetrics = g.getFontMetrics(errorFont);
        g.setFont(errorFont);
        g.setColor(Utils.colours[0]);

        if (attemptValidation == 2) {
            g.drawString("One of the fields may have been inputted incorrectly. Please try again.",
                    maxX / 2 - errorFontMetrics.stringWidth("One of the fields may have been inputted incorrectly. Please try again.") / 2,
                    (int) Math.round(0.75*maxY));
        } else if (attemptValidation == 1){
            g.drawString("Successfully added student",
                    maxX / 2 - errorFontMetrics.stringWidth("Successfully added student") / 2,
                    (int) Math.round(0.75*maxY));
        }

        repaint();
    }

    public void leaveScreen(int state) {
        attemptValidation = 0;
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        gradeField.setText("");
        display.changeState(state);
    }

    public boolean addStudent() throws exceptions.StudentAlreadyExistsException{
        int id = Integer.parseInt(idField.getText());
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        int grade = Integer.parseInt(gradeField.getText());
        if (idField.getText().length() != 9){
            return false;
        }
        return (signInManager.addStudent(id, firstName, lastName, grade));
    }

    private class MyMouseListener implements MouseListener {
        public void mouseEntered(MouseEvent e) {

        }

        public void mouseClicked(MouseEvent e){
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
                } catch (exceptions.StudentAlreadyExistsException error){
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
